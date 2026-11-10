package payment_system.entities;

import java.util.concurrent.atomic.AtomicInteger;

public class PaymentRequest {

    private static final AtomicInteger idCounter = new AtomicInteger(1);
    int id;
    String sender;
    String receiver;
    double amount;
    String currency;
    String idempotencyKey;
    private PaymentState paymentState;

    public PaymentRequest(String sender, String receiver, double amount, String currency, String idempotencyKey) {
        this.id = idCounter.getAndIncrement();
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.currency = currency;
        this.idempotencyKey = idempotencyKey;
        this.paymentState = new StartedState();
    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public String getPaymentStatus() {
        return paymentState.getState();
    }

    public PaymentState getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(PaymentState paymentState) {
        this.paymentState = paymentState;
    }

    public void validate() {
        paymentState.validate(this);
    }

    public void initiate() {
        paymentState.initiate(this);
    }

    public void confirm() {
        paymentState.confirm(this);
    }

    public void failed() {
        paymentState.failed(this);
    }

    public void retry() {
        paymentState.retry(this);
    }
}
