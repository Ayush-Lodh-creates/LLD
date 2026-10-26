package vending_machine.entities;

public class Slot {

    String id;
    String slotCode;
    private int quantity;
    Product product;

    public Slot(String id, String slotCode, int quantity, Product product) {
        this.id = id;
        this.slotCode = slotCode;
        setQuantity(quantity);
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlotCode() {
        return slotCode;
    }

    public void setSlotCode(String slotCode) {
        this.slotCode = slotCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be nonnegative");
        }
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
