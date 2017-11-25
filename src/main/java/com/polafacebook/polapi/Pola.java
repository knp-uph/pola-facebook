package com.polafacebook.polapi;

import com.google.gson.Gson;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Piotr on 09.07.2017.
 */
public class Pola {
    private String polaApiUrl = "https://www.pola-app.pl/a/v2/";
    private String deviceId = "";

    /**
     * Initializes and sets the URL string used to connect with Pola API.
     * @param polaApiUrl
     * @param deviceId
     */
    public Pola(String polaApiUrl, String deviceId) {
        this.polaApiUrl = polaApiUrl;
        this.deviceId = deviceId;
    }

    /**
     * Initializes and sets the URL string used to connect with Pola API.
     * @param polaApiUrl
     */
    public Pola(String polaApiUrl) {
        this.polaApiUrl = polaApiUrl;
    }


    /**
     * Initializes and sets the URL string used to connect with Pola API to the default value of "https://www.pola-app.pl/a/v2/".
     */
    public Pola() {}

    /**
     * Gets company information by product EAN code.
     * @param code
     * @return Result object with company info.
     * @throws IOException
     */
    public Result getByCode(String code) throws IOException {
        URL url = new URL(UriComponentsBuilder.fromUriString(polaApiUrl + "get_by_code")
                .queryParam("code", code)
                .queryParam("device_id", deviceId)
                .queryParam("noai")
                .build().toUriString());

        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");

        final int responseCode = httpConn.getResponseCode();
        if (responseCode != 200) {
            throw new PolaHttpException(url, responseCode);
        }

        BufferedReader r = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
        StringBuilder json = new StringBuilder();
        String line;

        while ((line = r.readLine()) != null) {
            json.append(line);
        }

        httpConn.disconnect();

        Result result = new Gson().fromJson(json.toString(), Result.class);
        return result;
    }

    /**
     * Builder class for constructing and submitting reports.
     * @return ReportBuilder object used to assemble reports set to the current Pola API address.
     */
    public ReportBuilder createReport() {
        return new ReportBuilder(this);
    }

    public class ReportBuilder {
        private Pola pola;
        private ReportRequest reportRequest = new ReportRequest();
        private Set<InputStream> fileStreams = new HashSet<InputStream>();
        private String deviceId = "";

        public ReportBuilder(Pola pola) {
            this.pola = pola;
            this.deviceId = pola.getDeviceId();
        }

        public ReportBuilder() {
        }

        private ReportBuilder setPola(Pola pola) {
            this.pola = pola;
            return this;
        }

        public ReportBuilder setDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public ReportBuilder setDescription(String description) {
            reportRequest.setDescription(description);
            return this;
        }

        public ReportBuilder setProductId(int id) {
            reportRequest.setProductId(id);
            return this;
        }

        public ReportBuilder setMimeType(String mimeType) {
            reportRequest.setMimeType(mimeType);
            return this;
        }

        public ReportBuilder setFileExtension(String fileExtension) {
            reportRequest.setFileExt(fileExtension);
            return this;
        }

        public ReportBuilder addFile(InputStream fileStream) {
            fileStreams.add(fileStream);
            reportRequest.setFilesCount(fileStreams.size());
            return this;
        }

        /**
         * Submits a product report based on attached data.
         * @return Id of the submitted report.
         * @throws IOException
         */
        public int send() throws IOException {
            ReportRequestResponse reportRequestResponse = sendReportRequest();
            sendReportFiles(reportRequestResponse);
            return reportRequestResponse.getId();
        }

        /**
         * Sends a report request to Pola.
         * @return Server response containing the report ID and the list of upload URIs.
         * @throws IOException
         */
        private ReportRequestResponse sendReportRequest() throws IOException {
            URL url = new URL(UriComponentsBuilder.fromUriString(polaApiUrl + "create_report")
                    .queryParam("device_id", deviceId)
                    .build().toUriString());
            Gson gson = new Gson();
            String reportJson = gson.toJson(reportRequest);

            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            httpConn.setUseCaches(false);

            httpConn.setRequestProperty("Content-Type", "application/json");
            DataOutputStream printout = new DataOutputStream(httpConn.getOutputStream());
            printout.writeBytes(reportJson);
            printout.close();

            int responseCode = httpConn.getResponseCode();
            if (responseCode != 200) {
                throw new PolaHttpException(url, responseCode);
            }

            //response reading code:
            StringBuilder responseJson = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                responseJson.append(line);
            }
            reader.close();

            responseCode = httpConn.getResponseCode();
            if (responseCode != 200) {
                throw new PolaHttpException(url, responseCode);
            }

            httpConn.disconnect();
            
            return gson.fromJson(responseJson.toString(), ReportRequestResponse.class);
        }

        /**
         * Uploads attached files to URIs specified in the ReportRequestResponse.
         * @param reportRequestResponse
         * @throws IOException
         */
        private void sendReportFiles(ReportRequestResponse reportRequestResponse) throws IOException {
            List<InputStream> fileStreamList = new ArrayList<>(fileStreams);
            String[] uploadUriList = reportRequestResponse.getSignedRequests();

            for (int i = 0; i < fileStreamList.size(); i++) {
                URL url = new URL(uploadUriList[i]);
                InputStream streamFrom = fileStreamList.get(i);

                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("PUT");
                httpConn.setRequestProperty("x-amz-acl", "public-read");
                httpConn.setRequestProperty("Content-Type", reportRequest.getMimeType());
                httpConn.setDoOutput(true);

                OutputStream streamTo = httpConn.getOutputStream();
                IOUtils.copy(streamFrom, streamTo);

                streamFrom.close();
                streamTo.close();

                final int responseCode = httpConn.getResponseCode();
                if (responseCode != 200) {
                    throw new PolaHttpException(url, responseCode);
                }

                httpConn.disconnect();
            }
        }
    }

    public String getPolaApiUrl() {
        return polaApiUrl;
    }

    public String getDeviceId() {
        return deviceId;
    }


    public static void main(String... args) throws IOException {
        Pola P = new Pola();
        try {
            //System.out.println(P.getByCode("5906441211478"));

            System.out.println(P.getByCode("5902759005488"));
//            System.out.println(P.createReport()
//                    .setDescription("test")
//                    .setDeviceId("test")
//                    .setProductId(305082)
//                    .setMimeType("image/png")
//                    .setFileExtension("png")
//                    .addFile(new FileInputStream(new File("D:/Piotr/Pictures/This is a test file.png")))
//                    .addFile(new FileInputStream(new File("D:/Piotr/Pictures/This is a test file 2.png")))
//                    .send());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
