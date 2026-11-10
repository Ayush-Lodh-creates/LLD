package payment_system;

import payment_system.entities.PaymentRequest;
import payment_system.enums.GatewayType;

import java.util.HashMap;
import java.util.Map;

public class PaymentController {

    private final PaymentService paymentService = PaymentService.getInstance();
    private final Map<String, PaymentRequest> idempotencyKeyToPaymentRequestMap = new HashMap<>();
    private static PaymentController instance;

    private PaymentController() {}

    public static PaymentController getInstance() {
        if (instance == null) {
            instance = new PaymentController();
        }
        return instance;
    }

    public PaymentRequest processPayment(String senderId, String receiverId, double amount, String currency, String orderId, GatewayType gatewayType) {
        if(idempotencyKeyToPaymentRequestMap.containsKey(orderId)) {
            PaymentRequest paymentRequest = idempotencyKeyToPaymentRequestMap.get(orderId);
            System.out.println("Duplicate request found with same orderId : " + paymentRequest.getId());
            return paymentRequest;
        }
        PaymentRequest paymentRequest = new PaymentRequest(senderId, receiverId, amount, currency, orderId);
        idempotencyKeyToPaymentRequestMap.put(orderId, paymentRequest);
        System.out.println("Processing payment request with id : " + paymentRequest.getId());
        paymentService.processPayment(paymentRequest, gatewayType, 3);
        return paymentRequest;
    }
}
