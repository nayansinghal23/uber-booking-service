package com.example.uberbookingservice.services;

import com.example.uberbookingservice.dto.CreateBookingRequestDTO;
import com.example.uberbookingservice.dto.CreateBookingResponseDTO;
import com.example.uberbookingservice.dto.GetAllBookingsRequestDTO;
import com.example.uberbookingservice.dto.GetAllBookingsResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    CreateBookingResponseDTO createBooking(CreateBookingRequestDTO dto);
    List<GetAllBookingsResponseDTO> getAllBookings(GetAllBookingsRequestDTO dto);
    void cancelBooking(Long bookingId);
}
