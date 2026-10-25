package com.example.uberbookingservice.dto;

import com.example.uberentityservice.models.Driver;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingResponseDTO {
    private Long bookingId;
    private String bookingStatus;
    private Driver driver;
}
