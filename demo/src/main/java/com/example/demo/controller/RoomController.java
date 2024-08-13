package com.example.demo.controller;

import com.example.demo.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllRooms(@RequestParam(required = false) Integer capacity) {
        try {
            return ResponseEntity.ok(roomService.getAllRooms(capacity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get rooms: " + e.getMessage());
        }
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> addRoom(@RequestBody Map<String, Object> roomInfo) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(roomService.addRoom(roomInfo).toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add room: " + e.getMessage());
        }
    }

    @PatchMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> editRoom(@RequestBody Map<String, Object> roomInfo) {
        try {
            return ResponseEntity.ok(roomService.editRoom(roomInfo).toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to edit room: " + e.getMessage());
        }
    }

    @DeleteMapping(produces = "application/json")
    public ResponseEntity<String> deleteRoom(@RequestParam(required = false) Long roomId) {
        try {
            if (roomId == null) {
                throw new IllegalArgumentException("Room ID is required");
            }
            return ResponseEntity.ok(roomService.deleteRoom(roomId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete room: " + e.getMessage());
        }
    }

    // Exception handler for handling IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
