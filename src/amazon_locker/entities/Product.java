package amazon_locker.entities;

import amazon_locker.enums.ProductSize;

public class Product {

    private String id;
    private String productName;
    private ProductSize productSize;

    public Product(String id, String productName, ProductSize productSize) {
        this.id = id;
        this.productName = productName;
        this.productSize = productSize;
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

    public ProductSize getProductSize() {
        return productSize;
    }

    public void setProductSize(ProductSize productSize) {
        this.productSize = productSize;
    }
}
