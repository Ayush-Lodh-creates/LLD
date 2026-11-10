package payment_system.gateway;

import payment_system.entities.PaymentRequest;

public abstract class PaymentGateway {

    public void processPayment(PaymentRequest paymentRequest) {
        validate(paymentRequest);
        initiate(paymentRequest);
        confirm(paymentRequest);
    }

    protected abstract void validate(PaymentRequest paymentRequest);
    protected abstract void initiate(PaymentRequest paymentRequest);
    protected abstract void confirm(PaymentRequest paymentRequest);
}
