package payment_system.entities;

public class ValidatedState implements PaymentState {

    @Override
    public String getState() {
        return "Validated";
    }

    @Override
    public void validate(PaymentRequest paymentRequest) {
        System.out.println(paymentRequest.getId() + " already in validated state");
    }

    @Override
    public void initiate(PaymentRequest paymentRequest) {
        System.out.println("Proceeding to initiate transefer for : " + paymentRequest.getId());
        paymentRequest.setPaymentState(new InitiatedState());
    }

    @Override
    public void confirm(PaymentRequest paymentRequest) {
        System.out.println("Cannot confirm payment unless payment is completed");
    }

    @Override
    public void failed(PaymentRequest paymentRequest) {
        System.out.println("Payment request failed in Validated state.");
        paymentRequest.setPaymentState(new FailedState());
    }

    @Override
    public void retry(PaymentRequest paymentRequest) {
        System.out.println("Something went wrong in Validated state, putting in retry");
        paymentRequest.setPaymentState(new RetryState());
    }
}
