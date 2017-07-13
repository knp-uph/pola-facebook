package com.polafacebook.polapi;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by Piotr on 09.07.2017.
 */
public class Pola {
    private String polaApiUrl = "https://www.pola-app.pl/a/v2/";

    /**
     * Initializes and sets the URL used to connect with Pola API.
     * @param polaApiUrl
     */
    public Pola(String polaApiUrl) {
        this.polaApiUrl = polaApiUrl;
    }

    public Pola() {
    }

    /**
     * Gets company information by product EAN code.
     * @param code
     * @param deviceId
     * @return Result object with company info
     * @throws IOException
     */
    public Result getByCode(String code, String deviceId) throws IOException {
        URL url = new URL(UriComponentsBuilder
                .fromUriString(polaApiUrl + "get_by_code")
                .queryParam("code", code)
                .queryParam("device_id", deviceId)
                .build().toUriString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        final int responseCode = conn.getResponseCode();
        BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            json.append(line);
        }
        Result result = new Gson().fromJson(json.toString(), Result.class);
        return result;
    }

    public ReportBuilder createReport() {
        return new ReportBuilder().setPola(this);
    }

    public class ReportBuilder {
        private Pola pola;
        private ReportRequest reportRequest = new ReportRequest();
        private ReportRequestResponse reportRequestResponse = new ReportRequestResponse();
        private Set<InputStream> fileStreams = new HashSet<InputStream>();
        private String deviceId;

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

        public void send() throws IOException {
            sendReportRequest();
            //TODO: send files
        }

        private void sendReportRequest() throws IOException {
            URL url = new URL(UriComponentsBuilder
                    .fromUriString(polaApiUrl + "create_report")
                    .queryParam("device_id", deviceId)
                    .build().toUriString());
            String reportJson = new Gson().toJson(reportRequest);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            writer.write(reportJson);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseJson = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseJson.append(line);
            }
            reportRequestResponse = new Gson().fromJson(responseJson.toString(), ReportRequestResponse.class);
        }

        private void sendReportFiles() throws IOException {
            List<InputStream> fileStreamList = new ArrayList<>(fileStreams);
            String[] uriList = reportRequestResponse.getSignedRequests();
            for (int i = 0; i < fileStreamList.size(); i++) {
                URL url = new URL(uriList[i]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("x-amz-acl", "public-read");
                conn.setRequestProperty("Content-Type", reportRequest.getMimeType());
                OutputStream outputStream = conn.getOutputStream();
                InputStream inputStream = fileStreamList.get(i);
                IOUtils.copy(inputStream, outputStream);    //from: https://stackoverflow.com/questions/43157/easy-way-to-write-contents-of-a-java-inputstream-to-an-outputstream
                inputStream.close();
                outputStream.close();
                conn.disconnect();
            }
        }
    }

    public String getPolaApiUrl() {
        return polaApiUrl;
    }

    public static void main(String... args) throws MalformedURLException {
        Pola P = new Pola();
        try {
            //System.out.println(P.getByCode("5906441211478", "DEVICE_ID"));
            P.createReport()
                    .setDescription("dupa")
                    .setDeviceId("test")
                    .setProductId(1)
                    .setMimeType("image/png")
                    .setFileExtension("png")
                    .send();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
