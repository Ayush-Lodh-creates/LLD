package payment_system.entities;

public class FailedState implements PaymentState {

    @Override
    public String getState() {
        return "Failed";
    }

    @Override
    public void validate(PaymentRequest paymentRequest) {
        System.out.println("Request has failed");
    }

    @Override
    public void initiate(PaymentRequest paymentRequest) {
        System.out.println("Request has failed");
    }

    @Override
    public void confirm(PaymentRequest paymentRequest) {
        System.out.println("Request has failed");
    }

    @Override
    public void failed(PaymentRequest paymentRequest) {
        System.out.println("Request has failed");
    }

    @Override
    public void retry(PaymentRequest paymentRequest) {
        System.out.println("Request has failed");
    }
}
