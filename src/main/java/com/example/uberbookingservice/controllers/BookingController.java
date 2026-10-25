package com.example.uberbookingservice.controllers;

import com.example.uberbookingservice.dto.CreateBookingRequestDTO;
import com.example.uberbookingservice.dto.CreateBookingResponseDTO;
import com.example.uberbookingservice.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<CreateBookingResponseDTO> createBooking(@RequestBody CreateBookingRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                bookingService.createBooking(dto)
        );
    }
}
