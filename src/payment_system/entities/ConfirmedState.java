package payment_system.entities;

public class ConfirmedState implements PaymentState {

    @Override
    public String getState() {
        return "Confirmed";
    }

    @Override
    public void validate(PaymentRequest paymentRequest) {
        System.out.println("Request already validated");
    }

    @Override
    public void initiate(PaymentRequest paymentRequest) {
        System.out.println("Payment request transfer already completed");
    }

    @Override
    public void confirm(PaymentRequest paymentRequest) {
        System.out.println("Request already in confirmed state");
    }

    @Override
    public void failed(PaymentRequest paymentRequest) {
        System.out.println("Cannot cancel request which is already completed");
    }

    @Override
    public void retry(PaymentRequest paymentRequest) {
        System.out.println("Cannot retry request which is already completed");
    }
}
