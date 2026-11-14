package com.example.uberbookingservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequestDTO {
    private Long passengerId;
    private LocationDTO startLocation;
    private LocationDTO endLocation;
}
