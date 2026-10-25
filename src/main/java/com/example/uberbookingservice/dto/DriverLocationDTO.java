package com.example.uberbookingservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverLocationDTO {
    private String driverId;
    private Double latitude;
    private Double longitude;
}