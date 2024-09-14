package com.app.TaxiReservation.dto.DistanceDto;

import lombok.Data;

import java.util.List;

@Data
public class DistanceResponseDto {
    private HintsDto hints;
    private InfoDto info;
    private List<PathDto> paths;
}
