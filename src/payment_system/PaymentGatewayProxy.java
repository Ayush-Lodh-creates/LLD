package payment_system;

import payment_system.entities.PaymentRequest;
import payment_system.enums.GatewayType;
import payment_system.gateway.PaymentGateway;

public class PaymentGatewayProxy {

    private PaymentGateway paymentGateway;
    int maxRetries;

    public PaymentGatewayProxy(int retries) {
        this.maxRetries = retries;
    }

    public void setPaymentGateway(GatewayType gatewayType) {
        PaymentGateway paymentGateway = GatewayFactory.getGateway(gatewayType);
        this.paymentGateway = paymentGateway;
    }

    public void processPayment(PaymentRequest paymentRequest) {
        if(paymentGateway == null) {
            throw new IllegalArgumentException("Payment gateway not set. Please set the payment gateway before processing payment.");
        }
        for(int retry = 0; retry < maxRetries; retry++) {
            try {
                paymentGateway.processPayment(paymentRequest);
                return; // Success, exit the retry loop
            } catch (Exception e) {
                paymentRequest.retry();
                if (retry == maxRetries - 1) {
                    paymentRequest.failed();
                    throw new RuntimeException("Failed to process payment after " + maxRetries + " attempts.", e);
                }
            }
        }
    }
}
