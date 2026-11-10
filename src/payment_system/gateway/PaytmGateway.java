package payment_system.gateway;

import payment_system.entities.PaymentRequest;

public class PaytmGateway extends PaymentGateway{

    @Override
    protected void validate(PaymentRequest paymentRequest) {
        // Paytm independent validation of payment request
        paymentRequest.validate();
        System.out.println("Validating payment request for Paytm: " + paymentRequest.getId());
    }

    @Override
    protected void initiate(PaymentRequest paymentRequest) {
        // Paytm independent initiation of payment request
        paymentRequest.initiate();
        System.out.println("Initiating payment for Paytm: " + paymentRequest.getId());
    }

    @Override
    protected void confirm(PaymentRequest paymentRequest) {
        // Paytm independent confirmation of payment request
        paymentRequest.confirm();
        System.out.println("Confirming payment for Paytm: " + paymentRequest.getId());
    }
}
