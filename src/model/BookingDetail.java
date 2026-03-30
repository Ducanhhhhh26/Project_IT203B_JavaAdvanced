package model;

public class BookingDetail {
    private int id;
    private int bookingId;
    private String type; // EQUIPMENT or SERVICE
    private int itemId; // equipment_id or service_id
    private int quantity;

    public BookingDetail() {}

    public BookingDetail(int id, int bookingId, String type, int itemId, int quantity) {
        this.id = id;
        this.bookingId = bookingId;
        this.type = type;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
