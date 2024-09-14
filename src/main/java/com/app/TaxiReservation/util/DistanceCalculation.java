package com.app.TaxiReservation.util;

import com.app.TaxiReservation.dto.DistanceDto.DistanceResponseDto;
import com.app.TaxiReservation.exception.RuntimeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class DistanceCalculation {

    private final String API_KEY = "fd28a38f-3be6-4ecf-9baa-74a193939921";

    public DistanceResponseDto getRoadDistance(double lat1, double lon1, double lat2, double lon2) {
        try {
            // Build the request URL for GraphHopper API
            String urlString = String.format("https://graphhopper.com/api/1/route?point=%f,%f&point=%f,%f&vehicle=car&key=%s",
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
