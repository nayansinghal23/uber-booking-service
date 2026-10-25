package com.example.uberbookingservice.services;

import com.example.uberbookingservice.dto.CreateBookingRequestDTO;
import com.example.uberbookingservice.dto.CreateBookingResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface BookingService {
    CreateBookingResponseDTO createBooking(CreateBookingRequestDTO dto);
}
