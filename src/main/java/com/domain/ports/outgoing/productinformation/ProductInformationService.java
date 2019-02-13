package com.domain.ports.outgoing.productinformation;

import java.io.IOException;

public interface ProductInformationService {
    public ProductInformation getByCode(String code) throws IOException;

    public ProductInformation getByCode(String code, String deviceId) throws IOException;

    public ReportBuilder createReport();
}
