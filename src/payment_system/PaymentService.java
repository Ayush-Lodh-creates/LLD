package payment_system;

import payment_system.entities.PaymentRequest;
import payment_system.enums.GatewayType;

public class PaymentService {

    private static PaymentService instance;

    private PaymentService() {}

    public static PaymentService getInstance() {
        if(instance == null) {
            instance = new PaymentService();
        }
        return instance;
    }

    public void processPayment(PaymentRequest paymentRequest, GatewayType gatewayType, int retries) {
        PaymentGatewayProxy paymentGatewayProxy = new PaymentGatewayProxy(retries);
        paymentGatewayProxy.setPaymentGateway(gatewayType);
        paymentGatewayProxy.processPayment(paymentRequest);
    }
}
