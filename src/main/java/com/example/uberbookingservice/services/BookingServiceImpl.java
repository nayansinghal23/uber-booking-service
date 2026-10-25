package com.example.uberbookingservice.services;

import com.example.uberbookingservice.dto.CreateBookingRequestDTO;
import com.example.uberbookingservice.dto.CreateBookingResponseDTO;
import com.example.uberbookingservice.dto.DriverLocationDTO;
import com.example.uberbookingservice.dto.NearbyDriversRequestDTO;
import com.example.uberbookingservice.helpers.CalculateTotalDistance;
import com.example.uberbookingservice.repositories.BookingRepository;
import com.example.uberbookingservice.repositories.PassengerRepository;
import com.example.uberentityservice.enums.BookingStatus;
import com.example.uberentityservice.models.Booking;
import com.example.uberentityservice.models.ExactLocation;
import com.example.uberentityservice.models.Passenger;
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
    private final RestTemplate restTemplate;
    private static final String LOCATION_SERVICE = "http://localhost:7476/api/v1/location";

    public BookingServiceImpl(BookingRepository bookingRepository, PassengerRepository passengerRepository, RestTemplate restTemplate) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public CreateBookingResponseDTO createBooking(CreateBookingRequestDTO dto) {
        ExactLocation startLocation = dto.getStartLocation();
        ExactLocation endLocation = dto.getEndLocation();

        long totalDistance = Math.round(
                CalculateTotalDistance.calculateDistance(
                        startLocation.getLatitude(),
                        startLocation.getLongitude(),
                        endLocation.getLatitude(),
                        endLocation.getLongitude()
                ) * 1000
        );
        // Assume average speed is 30 km/h
        double averageSpeedKmph = 30.0;
        long travelTimeMillis = (long) (
                (totalDistance / 1000.0) / averageSpeedKmph
                        * 60 * 60 * 1000
        );
        Date startTime = new Date();
        Date endTime = new Date(startTime.getTime() + travelTimeMillis);

        Optional<Passenger> passenger = passengerRepository.findById(dto.getPassengerId());
        if (passenger.isEmpty()) return null;

        Booking booking = Booking
                .builder()
                .bookingStatus(BookingStatus.ASSIGNING_DRIVER)
                .startLocation(startLocation)
                .endLocation(endLocation)
                .totalDistance(totalDistance)
                .startTime(startTime)
                .endTime(endTime)
                .passenger(passenger.get())
                .build();

        Booking newBooking = bookingRepository.save(booking);

        // Fetch nearby drivers
        NearbyDriversRequestDTO request = NearbyDriversRequestDTO
                .builder()
                .latitude(startLocation.getLatitude())
                .longitude(startLocation.getLongitude())
                .build();
        ResponseEntity<DriverLocationDTO[]> drivers = restTemplate.postForEntity(LOCATION_SERVICE + "/nearby/drivers", request, DriverLocationDTO[].class);

        if (drivers.getBody() != null) {
            List<DriverLocationDTO> driverLocations = Arrays.asList(drivers.getBody());
            driverLocations.forEach(loc -> {
                System.out.println("driver_id : " + loc.getDriverId() + " lat : " + loc.getLatitude() + " long : " + loc.getLongitude());
            });
        }


        return CreateBookingResponseDTO
                .builder()
                .bookingId(newBooking.getId())
                .bookingStatus(newBooking.getBookingStatus().toString())
                .build();
    }
}
