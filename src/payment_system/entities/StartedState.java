package payment_system.entities;

public class StartedState implements PaymentState {

    @Override
    public String getState() {
        return "Started";
    }

    @Override
    public void validate(PaymentRequest paymentRequest) {
        System.out.println("Validating payment request in Started state.");
        paymentRequest.setPaymentState(new ValidatedState());
    }

    @Override
    public void initiate(PaymentRequest paymentRequest) {
        System.out.println("Cannot initiate payment unless validated");
    }

    @Override
    public void confirm(PaymentRequest paymentRequest) {
        System.out.println("Cannot validate payment unless validated");
    }

    @Override
    public void failed(PaymentRequest paymentRequest) {
        System.out.println("Payment request failed in Started state.");
        paymentRequest.setPaymentState(new FailedState());
    }

    @Override
    public void retry(PaymentRequest paymentRequest) {
        System.out.println("Something went wrong in Started state, putting in retry");
        paymentRequest.setPaymentState(new RetryState());
    }
}
