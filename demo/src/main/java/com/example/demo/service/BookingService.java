package com.example.demo.service;

import com.example.demo.entity.Booking;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private Long lastAssignedBookingId = 0L;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        initializeLastAssignedBookingId(); // Initialize last assigned booking ID
    }

    private void initializeLastAssignedBookingId() {
        // Fetch the last assigned booking ID from the database
        Booking lastBooking = bookingRepository.findFirstByOrderByIdDesc();
        if (lastBooking != null) {
            lastAssignedBookingId = lastBooking.getId();
        }
    }

    // Generate next sequential booking ID
    private synchronized Long generateNextBookingId() {
        // Increment last assigned booking ID and return
        return ++lastAssignedBookingId;
    }

    public String createBooking(Map<String, Object> bookingInfo) {
        try {
            Long userId = Long.parseLong(bookingInfo.get("userID").toString());
            Long roomId = Long.parseLong(bookingInfo.get("roomID").toString());
            LocalDate dateOfBooking = LocalDate.parse((String) bookingInfo.get("dateOfBooking"));
            LocalTime timeFrom = LocalTime.parse((String) bookingInfo.get("timeFrom"));
            LocalTime timeTo = LocalTime.parse((String) bookingInfo.get("timeTo"));
            String purpose = (String) bookingInfo.get("purpose");

            // Check if user exists
            if (!userRepository.existsById(userId)) {
                return "User does not exist";
            }

            // Check if room exists
            if (!roomRepository.existsById(roomId)) {
                return "Room does not exist";
            }

            // Check if booking date is in the past
            if (dateOfBooking.isBefore(LocalDate.now())) {
                return "Failed to create booking";
            }

            // Check if booking time is in the past
            if (dateOfBooking.isEqual(LocalDate.now()) && timeFrom.isBefore(LocalTime.now())) {
                return "Failed to create booking";
            }

            // Check if room is available for the specified time slot
            List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(roomId, dateOfBooking, timeFrom, timeTo);
            if (!conflictingBookings.isEmpty()) {
                return "Room unavailable";
            }

            // Create new booking with next sequential booking ID
            Long bookingId = generateNextBookingId();
            Booking booking = new Booking();
            booking.setId(bookingId);
            booking.setUserId(userId);
            booking.setRoomId(roomId);
            booking.setDateOfBooking(dateOfBooking);
            booking.setTimeFrom(timeFrom);
            booking.setTimeTo(timeTo);
            booking.setPurpose(purpose);
            bookingRepository.save(booking);

            return "Booking created successfully";
        } catch (NumberFormatException | DateTimeParseException e) {
            return "Invalid input format";
        } catch (Exception e) {
            return "Failed to create booking: " + e.getMessage();
        }
    }

    public String editBooking(Map<String, Object> bookingInfo) {
        try {
            Long bookingId = Long.parseLong(bookingInfo.get("bookingID").toString());
            Long roomId = Long.parseLong(bookingInfo.get("roomID").toString());

            // Fetch existing booking
            Booking existingBooking = bookingRepository.findById(bookingId).orElse(null);
            if (existingBooking == null) {
                return "Booking does not exist";
            }

            // Check if room exists
            if (!roomRepository.existsById(roomId)) {
                return "Room does not exist";
            }

            // Update room ID for the booking
            existingBooking.setRoomId(roomId);
            bookingRepository.save(existingBooking);

            return "Booking modified successfully";
        } catch (NumberFormatException | DateTimeParseException e) {
            return "Invalid input format";
        } catch (Exception e) {
            return "Failed to edit booking: " + e.getMessage();
        }
    }

    public String deleteBooking(Long bookingId) {
        try {
            // Check if booking exists
            if (!bookingRepository.existsById(bookingId)) {
                return "Booking does not exist";
            }

            // Delete booking
            bookingRepository.deleteById(bookingId);

            return "Booking deleted successfully";
        } catch (Exception e) {
            return "Failed to delete booking: " + e.getMessage();
        }
    }
}
