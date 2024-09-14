package com.app.TaxiReservation.dto.DistanceDto;

import lombok.Data;

import java.util.List;

@Data
public class InfoDto {
    private List<String> copyrights;
    private Integer took;
    private String road_data_timestamp;
}
