package amazon_locker.entities;

import amazon_locker.enums.EventType;

public class Event {

    private String details;
    private String customerId;
    private String productName;
    private Integer accessCode;
    private EventType eventType;

    public Event(String customerId, String productName, Integer accessCode, EventType eventType) {
        this.customerId = customerId;
        this.productName = productName;
        this.accessCode = accessCode;
        this.eventType = eventType;
    }

    public String getDetails() { return details; }

    public void setDetails(String details) { this.details = details; }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(Integer accessCode) {
        this.accessCode = accessCode;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
