package com.adapters.outgoing.pola;

import com.adapters.outgoing.pola.converters.PolaResultToProductInformationConverter;
import com.domain.ports.outgoing.productinformation.ProductInformation;
import com.domain.ports.outgoing.productinformation.ProductInformationService;
import com.domain.ports.outgoing.productinformation.ReportBuilder;

import java.io.IOException;

public class PolaService implements ProductInformationService {
    private final Pola pola;

    private final PolaResultToProductInformationConverter polaResultToProductInformationConverter;

    public PolaService(Pola pola, PolaResultToProductInformationConverter polaResultToProductInformationConverter) {
        this.pola = pola;
        this.polaResultToProductInformationConverter = polaResultToProductInformationConverter;
    }

    @Override
    public ProductInformation getByCode(String code) throws IOException {
        return polaResultToProductInformationConverter.produceProductInformation(pola.getByCode(code, ""));
    }

    @Override
    public ProductInformation getByCode(String code, String deviceId) throws IOException {
        return polaResultToProductInformationConverter.produceProductInformation(pola.getByCode(code, deviceId));
    }

    @Override
    public ReportBuilder createReport() {
        return null;
    }
}
