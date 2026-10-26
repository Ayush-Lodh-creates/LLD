package vending_machine.entities;

public class Product {

    String id;
    String productName;
    private int cost;

    public Product(String id, String productName, int cost) {
        this.id = id;
        this.productName = productName;
        setCost(cost);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Cost must be nonnegative");
        }
        this.cost = cost;
    }
}
