package com.example.uberbookingservice.services;

import com.example.uberbookingservice.dto.CreateBookingRequestDTO;
import com.example.uberbookingservice.dto.CreateBookingResponseDTO;
import com.example.uberbookingservice.dto.DriverLocationDTO;
import com.example.uberbookingservice.dto.NearbyDriversRequestDTO;
import com.example.uberbookingservice.helpers.CalculateTotalDistance;
import com.example.uberbookingservice.repositories.BookingRepository;
import com.example.uberbookingservice.repositories.ExactLocationRepository;
import com.example.uberbookingservice.repositories.PassengerRepository;
import com.example.uberentityservice.enums.BookingStatus;
import com.example.uberentityservice.models.Booking;
import com.example.uberentityservice.models.ExactLocation;
import com.example.uberentityservice.models.Passenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final ExactLocationRepository exactLocationRepository;
    private final RestTemplate restTemplate;

    @Value("${location.service.base-url}")
    private String locationServiceUrl;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            PassengerRepository passengerRepository,
            ExactLocationRepository exactLocationRepository,
            RestTemplate restTemplate
    ) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
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

        try {
            String url = locationServiceUrl + "/nearby/drivers";
            var response = restTemplate.postForEntity(
                    url,
                    request,
                    DriverLocationDTO[].class
            );
            DriverLocationDTO[] drivers = response.getBody();
            if (drivers == null) {
                return List.of();
            }
            return Arrays.asList(drivers);
        } catch (Exception e) {
            return List.of();
        }
    }
}
