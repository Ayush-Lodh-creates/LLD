package payment_system;

import payment_system.enums.GatewayType;
import payment_system.gateway.PaymentGateway;
import payment_system.gateway.PaytmGateway;
import payment_system.gateway.RazorpayGateway;

public class GatewayFactory {

    private static final PaytmGateway paytmGateway = new PaytmGateway();
    private static final RazorpayGateway razorpayGateway = new RazorpayGateway();

    public static PaymentGateway getGateway(GatewayType gatewayType) {
        return switch (gatewayType) {
            case PAYTM -> paytmGateway;
            case RAZORPAY -> razorpayGateway;
            default -> throw new IllegalArgumentException("Unsupported gateway type");
        };
    }
}
