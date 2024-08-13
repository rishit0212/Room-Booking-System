package com.example.demo.repository;

import com.example.demo.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.roomId = :roomId AND b.dateOfBooking = :dateOfBooking " +
            "AND ((b.timeFrom <= :timeTo AND b.timeTo >= :timeFrom) OR " +
            "(b.timeFrom >= :timeFrom AND b.timeTo <= :timeTo))")
    List<Booking> findConflictingBookings(@Param("roomId") Long roomId,
                                          @Param("dateOfBooking") LocalDate dateOfBooking,
                                          @Param("timeFrom") LocalTime timeFrom,
                                          @Param("timeTo") LocalTime timeTo);

    @Query("SELECT b FROM Booking b WHERE b.id <> :bookingId AND b.roomId = :roomId AND b.dateOfBooking = :dateOfBooking " +
            "AND ((b.timeFrom <= :timeTo AND b.timeTo >= :timeFrom) OR " +
            "(b.timeFrom >= :timeFrom AND b.timeTo <= :timeTo))")
    List<Booking> findConflictingBookingsExcludingCurrent(@Param("bookingId") Long bookingId,
                                                          @Param("roomId") Long roomId,
                                                          @Param("dateOfBooking") LocalDate dateOfBooking,
                                                          @Param("timeFrom") LocalTime timeFrom,
                                                          @Param("timeTo") LocalTime timeTo);

    List<Booking> findUpcomingBookingsByUserId(Long userID);

    Booking findByUserId(Long userID);

    Booking findFirstByOrderByIdDesc();

    void deleteById(Long bookingId);

    List<Booking> findByUserIdAndDateOfBookingBefore(Long userId, LocalDate currentDate);

    List<Booking> findByUserIdAndDateOfBookingAfter(Long userId, LocalDate currentDate);

    List<Booking> findByUserIdAndDateOfBookingBeforeOrderByDateOfBookingDesc(Long userId, LocalDate currentDate);

    List<Booking> findByUserIdAndDateOfBookingAfterOrderByDateOfBookingAsc(Long userId, LocalDate currentDate);
}
