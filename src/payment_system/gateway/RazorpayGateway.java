package payment_system.gateway;

import payment_system.entities.PaymentRequest;

public class RazorpayGateway extends PaymentGateway {

    @Override
    protected void validate(PaymentRequest paymentRequest) {
        // Razorpay independent validation of payment request
        paymentRequest.validate();
        System.out.println("Validating payment request for Razorpay: " + paymentRequest.getId());
    }

    @Override
    protected void initiate(PaymentRequest paymentRequest) {
        // Razorpay independent initiation of payment request
        paymentRequest.initiate();
        System.out.println("Initiating payment for Razorpay: " + paymentRequest.getId());
    }

    @Override
    protected void confirm(PaymentRequest paymentRequest) {
        // Razorpay independent confirmation of payment request
        paymentRequest.confirm();
        System.out.println("Confirming payment for Razorpay: " + paymentRequest.getId());
    }
}
