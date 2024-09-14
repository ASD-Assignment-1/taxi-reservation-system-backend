package com.app.TaxiReservation.util;

import com.app.TaxiReservation.config.ApplicationProperties;
import com.app.TaxiReservation.dto.DistanceDto.DistanceResponseDto;
import com.app.TaxiReservation.exception.RuntimeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class DistanceCalculation {

    private final ApplicationProperties applicationProperties;
    private final String API_KEY;

    @Autowired
    public DistanceCalculation(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        this.API_KEY = applicationProperties.getApiKey();
    }

    public DistanceResponseDto getRoadDistance(double lat1, double lon1, double lat2, double lon2) {
        try {
            // Build the request URL for GraphHopper API
            String urlString = String.format(applicationProperties.getGraphhopperUrl(),
                    lat1, lon1, lat2, lon2, API_KEY);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            ObjectMapper objectMapper = new ObjectMapper();
            DistanceResponseDto routeResponse = objectMapper.readValue(content.toString(), DistanceResponseDto.class);

            return routeResponse;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
