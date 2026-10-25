package com.example.uberbookingservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearbyDriversRequestDTO {
    private Double latitude;
    private Double longitude;
}