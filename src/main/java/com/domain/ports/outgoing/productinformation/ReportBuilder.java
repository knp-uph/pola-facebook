package com.domain.ports.outgoing.productinformation;


import java.io.IOException;
import java.net.URL;

public interface ReportBuilder {
    ReportBuilder setDescription(String description);

    ReportBuilder setProductId(int id);

    ReportBuilder setMimeType(String mimeType);

    ReportBuilder setFileExtension(String fileExtension);

    ReportBuilder addFileFromUrl(URL url);

    int send() throws IOException;
}
