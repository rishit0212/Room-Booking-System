package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");
            String response = userService.login(email, password);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to login: " + e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Map<String, String> userDetails) {
        try {
            String email = userDetails.get("email");
            String name = userDetails.get("name");
            String password = userDetails.get("password");
            String response = userService.signup(email, name, password);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to signup: " + e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getUserDetails(@RequestParam Long userID) {
        try {
            return ResponseEntity.ok(userService.getUserDetails(userID));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get user details: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<Object> getRoomBookingHistory(@RequestParam Long userID) {
        try {
            return ResponseEntity.ok(userService.getRoomBookingHistory(userID));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get booking history: " + e.getMessage());
        }
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Object> getUpcomingRoomBookings(@RequestParam Long userID) {
        try {
            return ResponseEntity.ok(userService.getUpcomingRoomBookings(userID));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get upcoming bookings: " + e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get all users: " + e.getMessage());
        }
    }

    // Exception handler for handling general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
    }
}
