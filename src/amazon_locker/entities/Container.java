package amazon_locker.entities;

import amazon_locker.enums.ContainerSize;

public class Container {

    private String id;
    private String containerCode;
    private ContainerSize containerSize;
    private Boolean isBooked;
    private AccessCode accessCode;
    private Product product;

    public Container(String id, String containerCode, ContainerSize containerSize) {
        this.id = id;
        this.containerCode = containerCode;
        this.containerSize = containerSize;
        this.isBooked = false;
        this.accessCode = null;
        this.product = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public ContainerSize getContainerSize() {
        return containerSize;
    }

    public void setContainerSize(ContainerSize containerSize) {
        this.containerSize = containerSize;
    }

    public Boolean getBooked() {
        return isBooked;
    }

    public void setBooked(Boolean booked) {
        isBooked = booked;
    }

    public AccessCode getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(AccessCode accessCode) {
        this.accessCode = accessCode;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
