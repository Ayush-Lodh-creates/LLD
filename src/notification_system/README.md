# Notification System

This example models notification fan-out for registered clients. A client has subscribers such as email, SMS, or phone, and a notification carries its content, client ID, and severity.

The implementation focuses on separating message formatting from recipients and delivery policy. Actual delivery is represented by console output.

## Request flow

1. Register a `Client` and subscriber list with `NotificationService.onBoardClient()`.
2. Create a `SimpleNotification`, optionally wrapping it with `HeaderDecorator` and `SignatureDecorator`.
3. Call `processNotification()`. The service rejects an unregistered client, records the notification in memory, and calls each subscriber synchronously.
4. Each subscriber asks `DeliveryFactory` for the strategy matching the notification's severity, then passes its channel-specific message to that strategy.

## Responsibilities and patterns

[`NotificationService`](NotificationService.java) owns client registration, notification history, and fan-out. It is a singleton with synchronized initialization.

The [`Subscriber`](entities/subscriber/Subscriber.java) contract provides an observer-style boundary. Adding a channel means implementing `onEvent()` rather than putting channel branches into the service.

Decorators compose content while forwarding client ID and severity. Despite its name, `HeaderDecorator` appends its header after the wrapped content. Wrapping order therefore determines the final text order.

`DeliveryFactory` selects `LowStrategy`, `MediumStrategy`, or `HighStrategy`. These are extension points for different delivery policies, but all three currently perform the same console-printing operation. Severity does not currently control retries, urgency, channels, or scheduling.

## Concurrency and failure behavior

The service uses concurrent maps and a lock per client. The intended granularity is to serialize one client's notification processing while allowing different clients to proceed independently.

There are two gaps in that approach. First, onboarding creates locks using a separate contains-check and put, so concurrent first registrations for one client can use different locks. Second, locking is not paired with `try/finally`. If a subscriber throws, later subscribers are skipped and the lock can remain held, blocking other threads for that client.

Callbacks run while the client lock is held. Even without an exception, a slow subscriber delays subsequent notifications for the same client. A recorded notification also does not imply successful delivery to every subscriber.

## Current scope

- Registration stores the supplied subscriber list directly; repeat onboarding replaces it.
- Notification history is in memory and has no retention limit or public query API.
- Dynamic add/remove subscriber methods are not implemented.
- There is no queue, retry mechanism, delivery receipt, deduplication, or real email/SMS provider.

## Reading the example

There is no `Main` in this package. Start with `processNotification()`, then read `EmailSubscriber`, `DeliveryFactory`, and the decorators. To exercise it, onboard a client with an email subscriber and submit a `SimpleNotification` with the same client ID.

The next useful changes would be reliable lock management and subscriber failure isolation. After that, asynchronous delivery would introduce a meaningful discussion about ordering, retries, and what the service promises when a notification is accepted.
