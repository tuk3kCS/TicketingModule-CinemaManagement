package model;

public class Ticket {
    private int id;
    private int price;
    private Seat seat;
    private Schedule schedule;
    private int invoiceId;

    public Ticket() {}

    public Ticket(int id, int price, Seat seat, Schedule schedule, int invoiceId) {
        this.id = id;
        this.price = price;
        this.seat = seat;
        this.schedule = schedule;
        this.invoiceId = invoiceId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public Seat getSeat() { return seat; }
    public void setSeat(Seat seat) { this.seat = seat; }
    public Schedule getSchedule() { return schedule; }
    public void setSchedule(Schedule schedule) { this.schedule = schedule; }
    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }
} 