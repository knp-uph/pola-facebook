package com.adapters.outgoing.pola;

import com.adapters.outgoing.pola.converters.PolaResultToProductInformationConverter;
import com.domain.ports.outgoing.productinformation.ProductInformationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PolaConfiguration {

    @Bean
    public PolaService polaService(Pola pola, PolaResultToProductInformationConverter polaResultToProductInformationConverter) {
        return new PolaService(pola, polaResultToProductInformationConverter);
    }

    @Bean
    public Pola pola() {
        return new Pola(System.getenv("POLA_URL"));
    }

    @Bean
    public PolaResultToProductInformationConverter polaResultToProductInformationConverter() {
        return new PolaResultToProductInformationConverter();
    }

    @Bean
    public ProductInformationService productInformationService(PolaService polaService) {
        return polaService;
    }
}
