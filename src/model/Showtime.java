package model;

import java.util.Date;

public class Showtime {
    private int id;
    private Date time;

    public Showtime() {}

    public Showtime(int id, Date time) {
        this.id = id;
        this.time = time;

    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getTime() { return time; }
    public void setTime(Date time) { this.time = time; }
}