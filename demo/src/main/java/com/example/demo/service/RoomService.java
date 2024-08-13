package com.example.demo.service;

import com.example.demo.entity.Room;
import com.example.demo.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public String addRoom(String roomName, int roomCapacity) {
        try {
            // Check if room already exists
            if (roomRepository.findByName(roomName) != null) {
                return "Room already exists";
            }

            // Create new room
            Room room = new Room();
            room.setName(roomName);
            room.setCapacity(roomCapacity);
            roomRepository.save(room);

            return "Room created successfully";
        } catch (Exception e) {
            return "Failed to create room: " + e.getMessage();
        }
    }

    public String editRoom(Long roomId, String roomName, int roomCapacity) {
        try {
            // Check if room exists
            Room existingRoom = roomRepository.findById(roomId).orElse(null);
            if (existingRoom == null) {
                return "Room does not exist";
            }

            // Update room details
            existingRoom.setName(roomName);
            existingRoom.setCapacity(roomCapacity);
            roomRepository.save(existingRoom);

            return "Room edited successfully";
        } catch (Exception e) {
            return "Failed to edit room: " + e.getMessage();
        }
    }

    public String deleteRoom(Long roomId) {
        try {
            // Check if room exists
            if (!roomRepository.existsById(roomId)) {
                return "Room does not exist";
            }

            // Delete room
            roomRepository.deleteById(roomId);

            return "Room deleted successfully";
        } catch (Exception e) {
            return "Failed to delete room: " + e.getMessage();
        }
    }

    public List<Room> getAllRooms(Integer capacity) {
        try {
            if (capacity != null && capacity <= 0) {
                throw new IllegalArgumentException("Invalid capacity");
            }

            if (capacity != null) {
                return roomRepository.findByCapacity(capacity);
            } else {
                return roomRepository.findAll();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch rooms: " + e.getMessage());
        }
    }

    public Object addRoom(Map<String, Object> roomInfo) {
        try {
            String roomName = (String) roomInfo.get("roomName");
            int roomCapacity = (int) roomInfo.get("roomCapacity");

            // Check if room already exists
            Room existingRoom = roomRepository.findByName(roomName);
            if (existingRoom != null) {
                return "Room already exists";
            }

            // Create new room
            Room room = new Room();
            room.setName(roomName);
            room.setCapacity(roomCapacity);
            roomRepository.save(room);

            return "Room created successfully";
        } catch (Exception e) {
            return "Failed to create room: " + e.getMessage();
        }
    }

    public Object editRoom(Map<String, Object> roomInfo) {
        try {
            Long roomId = Long.parseLong((String) roomInfo.get("roomId"));
            String roomName = (String) roomInfo.get("roomName");
            int roomCapacity = (int) roomInfo.get("roomCapacity");

            // Check if room exists
            Room existingRoom = roomRepository.findById(roomId).orElse(null);
            if (existingRoom == null) {
                return "Room does not exist";
            }

            // Update room details
            existingRoom.setName(roomName);
            existingRoom.setCapacity(roomCapacity);
            roomRepository.save(existingRoom);

            return "Room edited successfully";
        } catch (Exception e) {
            return "Failed to edit room: " + e.getMessage();
        }
    }
}
