package model;

public class FoodItem {
    private int id;
    private String name;
    private String size;
    private double unitPrice;

    public FoodItem() {}

    public FoodItem(int id, String name, String size, double unitPrice) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.unitPrice = unitPrice;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
} 