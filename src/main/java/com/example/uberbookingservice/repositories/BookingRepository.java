package com.example.uberbookingservice.repositories;

import com.example.uberentityservice.enums.BookingStatus;
import com.example.uberentityservice.models.Booking;
import com.example.uberentityservice.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByPassengerId(Long passengerId);
    List<Booking> findAllByDriverId(Long driverId);

    @Modifying
    @Query("UPDATE Booking b SET b.bookingStatus = :bookingStatus WHERE b.id = :bookingId")
    int updateBookingStatus(@Param("bookingId") Long bookingId, @Param("bookingStatus") BookingStatus bookingStatus);

    @Modifying
    @Query("UPDATE Booking b SET b.driver = :driver WHERE b.id = :bookingId")
    int assignDriverToABooking(@Param("bookingId") Long bookingId, @Param("driver") Driver driver);
}
