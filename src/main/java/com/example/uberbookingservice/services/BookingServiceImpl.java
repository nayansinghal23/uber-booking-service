package com.example.uberbookingservice.services;

import com.example.uberbookingservice.dto.*;
import com.example.uberbookingservice.helpers.CalculateTotalDistance;
import com.example.uberbookingservice.repositories.BookingRepository;
import com.example.uberbookingservice.repositories.DriverRepository;
import com.example.uberbookingservice.repositories.ExactLocationRepository;
import com.example.uberbookingservice.repositories.PassengerRepository;
import com.example.uberentityservice.enums.BookingStatus;
import com.example.uberentityservice.enums.UserType;
import com.example.uberentityservice.models.Booking;
import com.example.uberentityservice.models.Driver;
import com.example.uberentityservice.models.ExactLocation;
import com.example.uberentityservice.models.Passenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final DriverRepository driverRepository;
    private final ExactLocationRepository exactLocationRepository;
    private final RestTemplate restTemplate;

    @Value("${location.service.base-url}")
    private String locationServiceUrl;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            PassengerRepository passengerRepository,
            DriverRepository driverRepository,
            ExactLocationRepository exactLocationRepository,
            RestTemplate restTemplate
    ) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.driverRepository = driverRepository;
        this.exactLocationRepository = exactLocationRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public CreateBookingResponseDTO createBooking(CreateBookingRequestDTO dto) {
        Passenger passenger = passengerRepository.findById(dto.getPassengerId()).orElseThrow(() -> new IllegalArgumentException("Passenger not found"));

        ExactLocation startLocation = ExactLocation.builder()
                .latitude(dto.getStartLocation().getLatitude())
                .longitude(dto.getStartLocation().getLongitude())
                .build();

        ExactLocation endLocation = ExactLocation.builder()
                .latitude(dto.getEndLocation().getLatitude())
                .longitude(dto.getEndLocation().getLongitude())
                .build();

        startLocation = exactLocationRepository.save(startLocation);
        endLocation = exactLocationRepository.save(endLocation);

        long totalDistance = Math.round(
                CalculateTotalDistance.calculateDistance(
                        startLocation.getLatitude(),
                        startLocation.getLongitude(),
                        endLocation.getLatitude(),
                        endLocation.getLongitude()
                ) * 1000
        );
        double averageSpeedKmph = 30.0;
        long travelTimeMillis = (long) (
                (totalDistance / 1000.0)
                        / averageSpeedKmph
                        * 60 * 60 * 1000
        );
        Date startTime = new Date();
        Date endTime = new Date(
                startTime.getTime() + travelTimeMillis
        );

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.ASSIGNING_DRIVER)
                .startLocation(startLocation)
                .endLocation(endLocation)
                .totalDistance(totalDistance)
                .startTime(startTime)
                .endTime(endTime)
                .passenger(passenger)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        List<DriverLocationDTO> nearbyDrivers = findNearbyDrivers(
                startLocation
        );

        return CreateBookingResponseDTO.builder()
                .bookingId(savedBooking.getId())
                .bookingStatus(savedBooking.getBookingStatus())
                .nearbyDrivers(nearbyDrivers)
                .build();
    }

    private List<DriverLocationDTO> findNearbyDrivers(ExactLocation startLocation) {
        NearbyDriversRequestDTO request =
                NearbyDriversRequestDTO.builder()
                        .latitude(startLocation.getLatitude())
                        .longitude(startLocation.getLongitude())
                        .build();

        String url = locationServiceUrl + "/api/v1/location/nearby/drivers";
        try {
            ResponseEntity<DriverLocationDTO[]> response =
                    restTemplate.postForEntity(
                            url,
                            request,
                            DriverLocationDTO[].class
                    );
            DriverLocationDTO[] drivers = response.getBody();
            if (drivers == null || drivers.length == 0) return List.of();

            return Arrays.asList(drivers);
        } catch (RestClientException exception) {
            return List.of();
        }
    }

    @Override
    public List<GetAllBookingsResponseDTO> getAllBookings(GetAllBookingsRequestDTO dto) {
        List<Booking> bookings = new ArrayList<>();
        if (dto.getUserType() == UserType.DRIVER) {
            Driver driver = driverRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalArgumentException("Driver not found"));
            bookings = bookingRepository.findAllByDriverId(dto.getUserId());
        } else {
            Passenger passenger = passengerRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalArgumentException("Passenger not found"));
            bookings = bookingRepository.findAllByPassengerId(dto.getUserId());
        }
        return bookings
                .stream()
                .map(GetAllBookingsResponseDTO::as)
                .toList();
    }
}
