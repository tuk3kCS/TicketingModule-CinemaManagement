package model;

import java.util.Date;
import java.util.List;

public class Invoice {
    private int id;
    private double total;
    private Date dateTime;
    private User user;
    private List<Ticket> ticketList;
    private List<OrderedFood> foodList;

    public Invoice() {}

    public Invoice(double total, Date dateTime, User user, List<Ticket> ticketList, List<OrderedFood> foodList) {
        this.total = total;
        this.dateTime = dateTime;
        this.user = user;
        this.ticketList = ticketList;
        this.foodList = foodList;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public Date getDateTime() { return dateTime; }
    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<Ticket> getTicketList() { return ticketList; }
    public void setTicketList(List<Ticket> ticketList) { this.ticketList = ticketList; }
    public List<OrderedFood> getFoodList() { return foodList; }
    public void setFoodList(List<OrderedFood> foodList) { this.foodList = foodList; }
} 