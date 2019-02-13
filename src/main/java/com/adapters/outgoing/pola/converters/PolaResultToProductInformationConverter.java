package com.adapters.outgoing.pola.converters;

import com.adapters.outgoing.pola.PolaResult;
import com.domain.ports.outgoing.productinformation.ProductInformation;

public class PolaResultToProductInformationConverter {
    public ProductInformation produceProductInformation(PolaResult polaResult) {
        return new ProductInformation(
                polaResult.getProductId(),
                polaResult.getCode(),
                polaResult.getName(),
                String.valueOf(polaResult.getPlScore()),
                polaResult.getPlCapital(),
                polaResult.isPlWorkers(),
                polaResult.isPlRnD(),
                polaResult.isPlRegistered(),
                polaResult.isPlNotGlobEnt(),
                polaResult.getDescription()
        );
    }
}
