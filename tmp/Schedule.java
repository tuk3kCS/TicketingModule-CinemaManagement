package model;

import java.util.Date;

public class Schedule {
    private int id;
    private Date date;
    private int movieId;
    private int screeningRoomId;
    private int showtimeId;

    public Schedule() {}

    public Schedule(int id, Date date, int movieId, int screeningRoomId, int showtimeId) {
        this.id = id;
        this.date = date;
        this.movieId = movieId;
        this.screeningRoomId = screeningRoomId;
        this.showtimeId = showtimeId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public int getScreeningRoomId() { return screeningRoomId; }
    public void setScreeningRoomId(int screeningRoomId) { this.screeningRoomId = screeningRoomId; }
    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }
} 