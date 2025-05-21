package model;

import java.util.Date;

public class Schedule {
    private int id;
    private Date date;
    private Movie movie;
    private Showtime showtime;
    private ScreeningRoom screeningRoom;

    public Schedule() {}

    public Schedule(int id, Date date, Movie movie, Showtime showtime, ScreeningRoom screeningRoom) {
        this.id = id;
        this.date = date;
        this.movie = movie;
        this.showtime = showtime;
        this.screeningRoom = screeningRoom;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }
    public Showtime getShowtime() { return showtime; }
    public void setShowtime(Showtime showtime) { this.showtime = showtime; }
    public ScreeningRoom getScreeningRoom() { return screeningRoom; }
    public void setScreeningRoom(ScreeningRoom screeningRoom) { this.screeningRoom = screeningRoom; }
} 
