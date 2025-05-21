package model;

public class OrderedFood {
    private int id;
    private int quantity;
    private int invoiceId;
    private FoodItem foodItem;

    public OrderedFood() {}

    public OrderedFood(int id, int quantity, int invoiceId, FoodItem foodItem) {
        this.id = id;
        this.quantity = quantity;
        this.invoiceId = invoiceId;
        this.foodItem = foodItem;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }
    public FoodItem getFoodItem() { return foodItem; }
    public void setFoodItem(FoodItem foodItem) { this.foodItem = foodItem; }
} 