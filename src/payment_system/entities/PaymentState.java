package payment_system.entities;

public interface PaymentState {
    
    String getState();
    void validate(PaymentRequest paymentRequest);
    void initiate(PaymentRequest paymentRequest);
    void confirm(PaymentRequest paymentRequest);
    void failed(PaymentRequest paymentRequest);
    void retry(PaymentRequest paymentRequest);
}
