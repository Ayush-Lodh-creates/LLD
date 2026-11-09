# Food Booking

This example follows a food order from selecting menu items to reserving stock and processing payment. The interesting part is the boundary between a mutable cart and an order that should retain the quantities it was created with.

## Scope

The implementation supports registering restaurants, finding one by an exact address match ignoring case, preparing a single-restaurant cart, and creating pickup or delivery orders. Payment is supplied by the caller through `PaymentStrategy`; there is no payment-provider integration or delivery tracking.

## Main objects

| Object | Responsibility |
| --- | --- |
| [`RestaurantManager`](RestaurantManager.java) | Restaurant lookup, cart validation, stock reservation, and stock restoration |
| [`Cart`](entities/Cart.java) | Selected restaurant, quantities, total, and pending checkout |
| [`Order`](entities/Order.java) | Order details, payment entry point, and state delegation |
| [`OrderFactory`](OrderFactory.java) | Creates `PickupOrder` or `DeliveryOrder` |
| `OrderState` | Payment-processing, completed, and cancelled behavior |

## Checkout flow

1. `bookFood()` checks that the selection is nonempty, that item codes exist, and that the user's cart is empty. It calculates a tentative total.
2. `Cart.confirmBooking()` calls `confirmCart()`. Each requested quantity is capped to available stock, inventory is reduced, and the total is recalculated.
3. The factory creates an order. `Map.copyOf()` takes a snapshot of the quantities, so later cart clearing does not erase the order's items.
4. `Order.processPayment()` invokes the supplied payment strategy. If it returns normally, the order moves from `PaymentProcessingState` to `CompletedState`.
5. The originating cart is cleared only if it still matches that pending order and its item quantities.

The state pattern keeps state-specific actions in small classes. The factory keeps order-type construction in one place. At present, pickup and delivery subclasses only set the order type; they do not have different fulfillment behavior.

## Decisions and limitations

Stock is reserved at confirmation, before payment. This avoids waiting until payment to account for inventory, but it needs a release policy for abandoned or failed checkouts. No timeout or automatic rollback is implemented. If the payment strategy throws, the order remains pending and stock remains reserved.

Partial fulfillment is allowed: requesting five units when only two remain produces an order for two. Zero-stock items can remain in the order with quantity zero. Positive quantity validation is still needed.

Cancellation while payment is pending restores stock. There is a mismatch in the completed-order path: `Order.cancel()` checks for `"Confirmed"`, while `CompletedState` reports `"Completed"`. Consequently, cancelling through `Order.cancel()` after payment does nothing. The refund message is not an implemented refund operation.

The concurrent restaurant map does not make checkout atomic. Mutable menu quantities, repeated confirmations, and singleton initialization need additional protection before supporting concurrent users. Prices currently use `double`.

## Reading and exercising the code

There is no `Main` in this package. Start with `RestaurantManager`, then follow `Cart.confirmBooking()` into `Order.processPayment()`. A small driver can create a restaurant and user, call `bookFood()`, confirm the cart with `OrderType.PICKUP` and a payment lambda, then process the returned order.

Useful scenarios to check are partial stock availability, a payment exception, pending cancellation, and repeated confirmation. These expose the transaction boundaries more clearly than adding another order subtype.
