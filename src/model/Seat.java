package model;

public class Seat {
    private int id;
    private String seatNumber;
    private int price;
    private int screeningRoomId;

    public Seat() {}

    public Seat(int id, String seatNumber, int price, int screeningRoomId) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.price = price;
        this.screeningRoomId = screeningRoomId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public int getScreeningRoomId() { return screeningRoomId; }
    public void setScreeningRoomId(int screeningRoomId) { this.screeningRoomId = screeningRoomId; }
} 