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
        return getByCode(code, "");
    }

    @Override
    public ProductInformation getByCode(String code, String deviceId) throws IOException {
        ProductInformation productInformation = polaResultToProductInformationConverter.produceProductInformation(pola.getByCode(code, ""));
        /* Adapter-level implementation detail:
        checking if description is null is one way of finding out if product information is valid.
         */
        if (productInformation.getDescription() == null) {
            productInformation = null;
        }
        return productInformation;
    }

    @Override
    public ReportBuilder createReport() {
        return pola.createReport();
    }
}
