package com.app.TaxiReservation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "taxi")
@Data
public class ApplicationProperties {

    private String apiKey;


}
