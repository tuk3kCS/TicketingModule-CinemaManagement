package model;

public class ScreeningRoom {
    private int id;
    private String roomName;
    private int numberOfSeats;
    private String roomType;

    public ScreeningRoom() {}

    public ScreeningRoom(int id, String roomName, int numberOfSeats, String roomType) {
        this.id = id;
        this.roomName = roomName;
        this.numberOfSeats = numberOfSeats;
        this.roomType = roomType;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public int getNumberOfSeats() { return numberOfSeats; }
    public void setNumberOfSeats(int numberOfSeats) { this.numberOfSeats = numberOfSeats; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
} 