package com.polafacebook.polapi;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by Piotr on 12.07.2017.
 */
public class ReportRequestResponse {
    private int id;
    @SerializedName("signed_requests")
    private String[] signedRequests;

    public ReportRequestResponse(int id, String[] signedRequests) {
        this.id = id;
        this.signedRequests = signedRequests;
    }

    public ReportRequestResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getSignedRequests() {
        return signedRequests;
    }

    public void setSignedRequests(String[] signedRequests) {
        this.signedRequests = signedRequests;
    }

    @Override
    public String toString() {
        return "ReportRequestResponse{" +
                "id=" + id +
                ", signedRequests=" + Arrays.toString(signedRequests) +
                '}';
    }
}
