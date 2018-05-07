package com.polafacebook.process.service.polapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Piotr on 12.07.2017.
 */
public class ReportRequest {
    private String description;
    @SerializedName("product_id")
    private int productId;
    @SerializedName("files_count")
    private int filesCount;
    @SerializedName("mime_type")
    private String mimeType;
    @SerializedName("file_ext")
    private String fileExt;

    public ReportRequest(String description, int productId, int filesCount, String mimeType, String fileExt) {
        this.description = description;
        this.productId = productId;
        this.filesCount = filesCount;
        this.mimeType = mimeType;
        this.fileExt = fileExt;
    }

    public ReportRequest() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(int filesCount) {
        this.filesCount = filesCount;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    @Override
    public String toString() {
        return "ReportRequest{" +
                "description='" + description + '\'' +
                ", productId=" + productId +
                ", filesCount=" + filesCount +
                ", mimeType='" + mimeType + '\'' +
                ", fileExt='" + fileExt + '\'' +
                '}';
    }
}
