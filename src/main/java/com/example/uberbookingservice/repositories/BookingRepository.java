package com.example.uberbookingservice.repositories;

import com.example.uberentityservice.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByPassengerId(Long passengerId);
    List<Booking> findAllByDriverId(Long driverId);
}
