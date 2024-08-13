package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.entity.Booking;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public UserService(UserRepository userRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    public String login(String email, String password) {
        try {
            // Check if user exists
            User user = userRepository.findByEmail(email);
            if (user == null) {
                return "User does not exist";
            }

            // Check if password matches
            if (!user.getPassword().equals(password)) {
                return "Username/Password Incorrect";
            }

            return "Login Successful";
        } catch (Exception e) {
            return "Failed to login: " + e.getMessage();
        }
    }

    public String signup(String email, String name, String password) {
        try {
            // Check if user already exists
            if (userRepository.existsByEmail(email)) {
                return "Forbidden, Account already exists";
            }

            // Create new user
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPassword(password);
            userRepository.save(user);

            return "Account Creation Successful";
        } catch (Exception e) {
            return "Failed to signup: " + e.getMessage();
        }
    }

    public Object getUserDetails(Long userID) {
        try {
            Optional<User> optionalUser = userRepository.findById(userID);
            if (optionalUser.isEmpty()) {
                return "User does not exist";
            }
            User user = optionalUser.get();
            user.setPassword(null); // Exclude password from user details
            return user;
        } catch (Exception e) {
            return "Failed to fetch user details: " + e.getMessage();
        }
    }

    public List<Booking> getRoomBookingHistory(Long userId) {
        try {
            // Get current date
            LocalDate currentDate = LocalDate.now();

            // Fetch booking history for the user before the current date
            return bookingRepository.findByUserIdAndDateOfBookingBeforeOrderByDateOfBookingDesc(userId, currentDate);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch booking history: " + e.getMessage());
        }
    }


    public List<Booking> getUpcomingRoomBookings(Long userId) {
        try {
            // Get current date
            LocalDate currentDate = LocalDate.now();

            // Fetch upcoming bookings for the user
            return bookingRepository.findByUserIdAndDateOfBookingAfterOrderByDateOfBookingAsc(userId, currentDate);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch upcoming room bookings: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            // Remove password field from each user object
            users.forEach(user -> user.setPassword(null));
            return users;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch all users: " + e.getMessage());
        }
    }
}
