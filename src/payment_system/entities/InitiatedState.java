package payment_system.entities;

public class InitiatedState implements PaymentState {

    @Override
    public String getState() {
        return "Initiated";
    }

    @Override
    public void validate(PaymentRequest paymentRequest) {
        System.out.println("Payment request : " + paymentRequest.getId() + " already validated");
    }

    @Override
    public void initiate(PaymentRequest paymentRequest) {
        System.out.println("Request already initiated");
    }

    @Override
    public void confirm(PaymentRequest paymentRequest) {
        System.out.println("Confirming payment for request: " + paymentRequest.getId());
        paymentRequest.setPaymentState(new ConfirmedState());
    }

    @Override
    public void failed(PaymentRequest paymentRequest) {
        System.out.println("Payment request failed in Initiated state.");
        paymentRequest.setPaymentState(new FailedState());
    }

    @Override
    public void retry(PaymentRequest paymentRequest) {
        System.out.println("Something went wrong in Initiated state, putting in retry");
        paymentRequest.setPaymentState(new RetryState());
    }
}
