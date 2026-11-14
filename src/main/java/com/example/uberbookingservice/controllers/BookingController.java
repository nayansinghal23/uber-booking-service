package com.example.uberbookingservice.controllers;

import com.example.uberbookingservice.dto.CreateBookingRequestDTO;
import com.example.uberbookingservice.dto.CreateBookingResponseDTO;
import com.example.uberbookingservice.dto.GetAllBookingsRequestDTO;
import com.example.uberbookingservice.dto.GetAllBookingsResponseDTO;
import com.example.uberbookingservice.services.BookingService;
import com.example.uberentityservice.models.Booking;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<CreateBookingResponseDTO> createBooking(@RequestBody CreateBookingRequestDTO dto) {
        try {
            CreateBookingResponseDTO booking = bookingService.createBooking(dto);
            return new ResponseEntity<>(booking, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    private ResponseEntity<List<GetAllBookingsResponseDTO>> getAllBookings(@RequestBody GetAllBookingsRequestDTO dto) {
        try {
            List<GetAllBookingsResponseDTO> bookings = bookingService.getAllBookings(dto);
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{bookingId}")
    private ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        try {
            bookingService.cancelBooking(bookingId);
            return new ResponseEntity<>("Booking has been cancelled", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
