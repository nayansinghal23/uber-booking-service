package com.example.uberbookingservice.repositories;

import com.example.uberentityservice.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
