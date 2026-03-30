package model;

public class Equipment {
    private int id;
    private String name;
    private int totalQuantity;
    private int availableQuantity;
    private String status;

    public Equipment() {}

    public Equipment(int id, String name, int totalQuantity, int availableQuantity, String status) {
        this.id = id;
        this.name = name;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
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
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
