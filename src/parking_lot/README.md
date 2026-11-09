# Parking Lot

This case study covers slot allocation and the lifecycle of a parking ticket, including concurrent bookings, reservation expiry, payment, and exit. It also separates price calculation and availability updates from allocation.

## Scope

There are two slot categories: two-wheeler and four-wheeler. Regular cars use one four-wheeler slot; a large vehicle requires multiple consecutively numbered four-wheeler slots. The implementation supports reservations and on-demand bookings, rejects duplicate active bookings for a vehicle number, and allows inventory to grow or shrink by adding or removing free slots.

Everything is in memory. Payment and audit output are simulated.

## Allocation and ticket flow

[`Parking`](entities/Parking.java) coordinates the operation. It first registers a ticket in `bookedTickets` with `putIfAbsent()`, so competing requests for the same vehicle cannot both proceed. Failed allocation removes this registration so a later attempt is possible.

For a large vehicle, the allocator looks for an uninterrupted sequence of slot codes and acquires the candidate slot locks before marking the group booked. Removing a slot does not make the spaces on either side appear adjacent.

A reservation starts in `PendingState` and holds its slots for up to 15 minutes. `processPayment()` checks expiry before moving it to `CompletedState`. `cancelTicket()` releases a pending reservation; `expireTickets()` scans for overdue reservations. Both cancellation and expiry use `CancelState`, with different event types to distinguish the outcome.

On-demand bookings complete the simulated payment during booking. A paid, active ticket can exit through `freeSlots()`, which releases its allocation and moves it to `ExitedState`. Ticket identity checks prevent an old ticket from releasing a newer booking for the same vehicle.

## Concurrency

An inventory read/write lock protects the slot-list structure: allocation takes a read lock, while adding or removing slots takes a write lock. Individual slot locks protect allocation flags. Allocation uses `tryLock()`, so it can skip a busy candidate and report temporary unavailability instead of waiting.

Ticket locks serialize lifecycle operations such as payment, cancellation, expiry, and exit. Concurrent maps alone would not protect these multi-step changes. Callers should use the `Parking` API; mutable ticket and slot setters can bypass its invariants.

## Pricing and events

`BasePrice` delegates to `HourlyPriceStrategy` or `PeakPriceStrategy`. Hourly pricing uses rounded-up elapsed hours: a base of 25 for two-wheelers, 50 per occupied slot for large vehicles, and zero base for other vehicles, plus 5 per hour. Peak pricing adds 15% using integer arithmetic.

`EVDecorator` adds 5 and `ValetDecorator` adds 10. A membership discount prints a 20% reduction; a flat discount prints a reduction of 15. These are quotes: `processPayment()` does not consume or record the calculated amount. The EV decorator's code adds 5 despite its stale comment mentioning 20.

Observers receive allocation and lifecycle events. `BoardDisplayListener` updates availability counts, while `AuditListener` prints event details. A listener exception is caught so it does not undo the parking operation. Callbacks still run synchronously, so slow listeners can delay operations.

## Try it and remaining work

Run [`parking_lot.Main`](Main.java). It submits concurrent bookings, including a duplicate vehicle, then demonstrates payment and exit, cancellation, and simulated reservation expiry. Final availability should return to two two-wheeler and eight four-wheeler slots; console ordering can vary.

Expiry scanning is caller-driven, not scheduled in the background. There are no floors, gates, durable records, real payments, or refund handling. A useful next step is joining the quote and payment flows into a recorded bill, then making expiry an explicit service responsibility.
