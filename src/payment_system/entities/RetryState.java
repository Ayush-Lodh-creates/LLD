package payment_system.entities;

public class RetryState implements PaymentState {

    @Override
    public String getState() {
        return "Retrying";
    }

    @Override
    public void validate(PaymentRequest paymentRequest) {
        System.out.println("Retrying payment request putting back to validation step");
        paymentRequest.setPaymentState(new ValidatedState());
    }

    @Override
    public void initiate(PaymentRequest paymentRequest) {
        System.out.println("Retrying payment request putting back to initiation step");
        paymentRequest.setPaymentState(new InitiatedState());
    }

    @Override
    public void confirm(PaymentRequest paymentRequest) {
        System.out.println("Cannot put request in confirm state unless validated and transfer is completed");
    }

    @Override
    public void failed(PaymentRequest paymentRequest) {
        System.out.println("Request has failed");
        paymentRequest.setPaymentState(new FailedState());
    }

    @Override
    public void retry(PaymentRequest paymentRequest) {
        System.out.println("Cannot retry request which is already in retry state");
    }
}
