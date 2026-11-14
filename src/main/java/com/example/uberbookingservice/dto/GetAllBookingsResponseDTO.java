package com.example.uberbookingservice.dto;

import com.example.uberentityservice.enums.BookingStatus;
import com.example.uberentityservice.models.Booking;
import com.example.uberentityservice.models.ExactLocation;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllBookingsResponseDTO {
    private Long bookingId;
    private BookingStatus bookingStatus;
    private Date startTime;
    private Date endTime;
    private Long totalDistance;
    private Long driverId;
    private Long passengerId;
    private ExactLocation startLocation;
    private ExactLocation endLocation;
    private Date createdAt;
    private Date updatedAt;

    public static GetAllBookingsResponseDTO as(Booking booking) {
        return GetAllBookingsResponseDTO
                .builder()
                .bookingId(booking.getId())
                .bookingStatus(booking.getBookingStatus())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .totalDistance(booking.getTotalDistance())
                .driverId(booking.getDriver() != null ? booking.getDriver().getId() : null)
                .passengerId(booking.getPassenger().getId())
                .startLocation(booking.getStartLocation())
                .endLocation(booking.getEndLocation())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}
