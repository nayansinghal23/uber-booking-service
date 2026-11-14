package com.example.uberbookingservice.dto;

import com.example.uberentityservice.enums.BookingStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingResponseDTO {
    private Long bookingId;
    private BookingStatus bookingStatus;
    private List<DriverLocationDTO> nearbyDrivers;
}