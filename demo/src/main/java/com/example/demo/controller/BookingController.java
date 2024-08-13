package com.example.demo.controller;

import com.example.demo.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/book")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createBooking(@RequestBody Map<String, Object> bookingInfo) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(bookingInfo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create booking: " + e.getMessage());
        }
    }

    @PatchMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> editBooking(@RequestBody Map<String, Object> bookingInfo) {
        try {
            return ResponseEntity.ok(bookingService.editBooking(bookingInfo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to edit booking: " + e.getMessage());
        }
    }

    @DeleteMapping(produces = "application/json")
    public ResponseEntity<String> deleteBooking(@RequestParam(required = false) Long bookingId) {
        try {
            if (bookingId == null) {
                throw new IllegalArgumentException("Booking ID is required");
            }
            return ResponseEntity.ok(bookingService.deleteBooking(bookingId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete booking: " + e.getMessage());
        }
    }

    // Exception handler for handling IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
