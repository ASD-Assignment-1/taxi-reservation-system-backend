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

    /*public String getLocationName(double latitude, double longitude) {
        try {
            // Build the Nominatim API URL
            String urlString = String.format("https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=%f&lon=%f", latitude, longitude);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");  // Set a user agent to avoid potential blocks

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(content.toString());

            // Extract the display name (location name)
            String locationName = jsonNode.get("display_name").asText();
            return locationName;

        } catch (Exception e) {
            throw new java.lang.RuntimeException("Error while getting location name: " + e.getMessage());
        }
    }*/
}
