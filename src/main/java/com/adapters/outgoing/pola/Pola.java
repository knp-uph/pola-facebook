package com.adapters.outgoing.pola;

import com.google.gson.Gson;
import okhttp3.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Piotr on 09.07.2017.
 */
class Pola {
    private String polaApiUrl;

    /**
     * Initializes and sets the URL string used to connect with Pola API.
     *
     * @param polaApiUrl
     * @param deviceId
     */
    public Pola(String polaApiUrl, String deviceId) {
        this.polaApiUrl = polaApiUrl;
    }

    /**
     * Initializes and sets the URL string used to connect with Pola API.
     *
     * @param polaApiUrl
     */
    public Pola(String polaApiUrl) {
        this.polaApiUrl = polaApiUrl;
    }

    public String getPolaApiUrl() {
        return polaApiUrl;
    }

    /**
     * Gets company information by product EAN code.
     *
     * @param code
     * @return PolaResult object with company info.
     * @throws IOException
     */
    public PolaResult getByCode(String code, String deviceId) throws IOException {
        URL url = new URL(UriComponentsBuilder.fromUriString(polaApiUrl + "get_by_code")
                .queryParam("code", code)
                .queryParam("device_id", deviceId)
                .queryParam("noai")
                .build().toUriString());

        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");

        final int responseCode = httpConn.getResponseCode();
        if (responseCode != 200) {
            throw new PolaHttpException(url.toString(), responseCode);
        }

        BufferedReader r = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
        StringBuilder json = new StringBuilder();
        String line;

        while ((line = r.readLine()) != null) {
            json.append(line);
        }

        httpConn.disconnect();

        PolaResult result = new Gson().fromJson(json.toString(), PolaResult.class);
        return result;
    }

    /**
     * Builder class for constructing and submitting reports.
     *
     * @return ReportBuilder object used to assemble reports set to the current Pola API address.
     */
    public ReportBuilder createReport() {
        return new ReportBuilder(this);
    }

    public class ReportBuilder implements com.domain.ports.outgoing.productinformation.ReportBuilder {
        private Pola pola;
        private ReportRequest reportRequest = new ReportRequest();
        private Set<URL> imageAttachmentUrls = new HashSet<>();
        private String deviceId = "";

        private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        private final OkHttpClient client = new OkHttpClient();

        public ReportBuilder(Pola pola) {
            this.pola = pola;
        }

        private ReportBuilder setPola(Pola pola) {
            this.pola = pola;
            return this;
        }

        public ReportBuilder setDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        @Override
        public ReportBuilder setDescription(String description) {
            reportRequest.setDescription(description);
            return this;
        }

        @Override
        public ReportBuilder setProductId(int id) {
            reportRequest.setProductId(id);
            return this;
        }

        @Override
        public ReportBuilder setMimeType(String mimeType) {
            reportRequest.setMimeType(mimeType);
            return this;
        }

        @Override
        public ReportBuilder setFileExtension(String fileExtension) {
            reportRequest.setFileExt(fileExtension);
            return this;
        }

        @Override
        public ReportBuilder addFileFromUrl(URL url) {
            imageAttachmentUrls.add(url);
            reportRequest.setFilesCount(imageAttachmentUrls.size());
            return this;
        }

        /**
         * Submits a product report based on attached data.
         *
         * @return Id of the submitted report.
         * @throws IOException
         */
        @Override
        public int send() throws IOException {
            ReportRequestResponse reportRequestResponse = sendReportRequest();
            sendReportFiles(reportRequestResponse);
            return reportRequestResponse.getId();
        }

        /**
         * Sends a report request to Pola.
         *
         * @return Server response containing the report ID and the list of upload URIs.
         * @throws IOException
         */
        private ReportRequestResponse sendReportRequest() throws IOException {
            URL url = new URL(UriComponentsBuilder.fromUriString(polaApiUrl + "create_report")
                    .queryParam("device_id", deviceId)
                    .build().toUriString());
            Gson gson = new Gson();
            String reportJson = gson.toJson(reportRequest);

            RequestBody body = RequestBody.create(JSON, reportJson);
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new PolaHttpException(url.toString(), response.code());
                }
                return gson.fromJson(response.body().string(), ReportRequestResponse.class);
            }
        }

        /**
         * Uploads attached files to URIs specified in the ReportRequestResponse.
         *
         * @param reportRequestResponse
         * @throws IOException
         */
        private void sendReportFiles(ReportRequestResponse reportRequestResponse) throws IOException {
            String[] uploadUriList = reportRequestResponse.getSignedRequests();
            Iterator<URL> attachmentIterator = imageAttachmentUrls.iterator();

            for (int i = 0; i < uploadUriList.length; i++) {
                Request downloadRequest = new Request.Builder()
                        .url(attachmentIterator.next())
                        .build();

                try (Response downloadResponse = client.newCall(downloadRequest).execute()) {
                    if (!downloadResponse.isSuccessful()) {
                        throw new PolaHttpException(downloadRequest.url().toString(), downloadResponse.code());
                    }

                    ResponseBody downloadedBody = downloadResponse.body();

                    HttpURLConnection httpConn = (HttpURLConnection) new URL(uploadUriList[i]).openConnection();
                    httpConn.setRequestProperty("x-amz-acl", "public-read");
                    httpConn.setRequestMethod("PUT");
                    httpConn.setDoOutput(true);
                    httpConn.setUseCaches(false);

                    httpConn.setRequestProperty("Content-Type", reportRequest.getMimeType());
                    DataOutputStream printout = new DataOutputStream(httpConn.getOutputStream());
                    byte[] downloadedContentBytes = downloadedBody.bytes();
                    printout.write(downloadedContentBytes, 0, downloadedContentBytes.length);
                    printout.close();

                    int responseCode = httpConn.getResponseCode();
                    if (responseCode != 200) {
                        throw new PolaHttpException(uploadUriList[i], responseCode);
                    }

                    httpConn.disconnect();
                }
            }
        }
    }

    public static void main(String... args) throws IOException {
        Pola P = new Pola("https://www.pola-app.pl/a/v2/");
        //System.out.println(P.getByCode("5906441211478"));
        //System.out.println(P.getByCode("1234567890123"));
        System.out.println(P.createReport()
                .setDescription("test")
                .setDeviceId("test")
                .setProductId(5101323)
                .setMimeType("image/png")
                .setFileExtension("png")
                .addFileFromUrl(new URL("https://scontent-iad3-1.xx.fbcdn.net/v/t39.1997-6/851557_369239266556155_759568595_n.png?_nc_cat=0&_nc_ad=z-m&_nc_cid=0&oh=077377daae3942a4ec47da063d057ae0&oe=5B8AF8DC"))
                .send());
    }
}
