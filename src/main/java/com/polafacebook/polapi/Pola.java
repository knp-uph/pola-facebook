package com.polafacebook.polapi;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;

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
     * @return Result object with company info.
     * @throws IOException
     */
    public Result getByCode(String code, String deviceId) throws IOException {
        URL url = new URL(UriComponentsBuilder.fromUriString(polaApiUrl + "get_by_code")
                .queryParam("code", code)
                .queryParam("device_id", deviceId).build().toUriString());
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        final int responseCode = httpConn.getResponseCode();
        BufferedReader r = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            json.append(line);
        }
        Result result = new Gson().fromJson(json.toString(), Result.class);
        return result;
    }

    /**
     *
     * @return ReportBuilder object used to assemble reports set to the current Pola API address.
     */
    public ReportBuilder createReport() {
        return new ReportBuilder().setPola(this);
    }

    public class ReportBuilder {
        private Pola pola;
        private ReportRequest reportRequest = new ReportRequest();
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
                    .queryParam("device_id", deviceId).build().toUriString());
            Gson gson = new Gson();
            String reportJson = gson.toJson(reportRequest);
            URLConnection urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/json");
            DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
            printout.writeBytes(reportJson);
            printout.close();
            //response reading code:
            StringBuilder responseJson = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                responseJson.append(line);
            }
            reader.close();
            System.out.println(responseJson);
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
                httpConn.setDoOutput(true);
                httpConn.setRequestMethod("PUT");
                httpConn.setRequestProperty("x-amz-acl", "public-read");
                httpConn.setRequestProperty("Content-Type", reportRequest.getMimeType());
                OutputStream streamTo = httpConn.getOutputStream();
                IOUtils.copy(streamFrom, streamTo);
                streamFrom.close();
                streamTo.close();
                httpConn.disconnect();
            }
        }
    }

    public String getPolaApiUrl() {
        return polaApiUrl;
    }
}
