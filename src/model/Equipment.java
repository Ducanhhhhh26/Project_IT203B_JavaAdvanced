package model;

public class Equipment {
    private int id;
    private String name;
    private int totalQuantity;
    private int availableQuantity;
    private double rentalPrice;
    private String status;

    public Equipment() {}

    public Equipment(int id, String name, int totalQuantity, int availableQuantity, double rentalPrice, String status) {
        this.id = id;
        this.name = name;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
        this.rentalPrice = rentalPrice;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }
    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }
    public double getRentalPrice() { return rentalPrice; }
    public void setRentalPrice(double rentalPrice) { this.rentalPrice = rentalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
