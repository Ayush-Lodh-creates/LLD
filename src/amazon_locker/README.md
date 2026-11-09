# Amazon Locker

This case study models a locker station where a delivery agent deposits a package and a customer collects it using an access code. The main design question is how to keep compartment allocation, pickup, and notifications separate without making the deposit flow hard to follow.

## Scope

- Small, medium, and large compartments, with one package per compartment.
- Allocation to the smallest available size that fits the package.
- Delivery-agent deposits and customer pickups using a compartment code and a four-digit access code.
- Explicit cleanup of expired packages, plus audit events and customer notifications.
- Adding compartments and removing an unoccupied compartment.

Everything is held in memory. The default email sender prints to the console.

## Design and flow

[`Locker`](entities/Locker.java) coordinates the operation and keeps an index of occupied compartments. A [`Container`](entities/Container.java) holds its package, booking flag, and access code. `ProductSize` and `ContainerSize` define the size categories.

For a deposit, `Locker` checks the agent's role, asks `ContainerSelectionStrategy` for a compartment, stores the package, generates an access code with a seven-day expiry, and publishes a deposit event. Prefer the overload that accepts both the delivery agent and customer: it records the intended recipient for notifications.

[`GreedyContainerStrategy`](strategy/GreedyContainerStrategy.java) scans eligible sizes in increasing order. A small package can use a medium or large compartment when smaller ones are occupied. Keeping this behind a strategy makes allocation policy replaceable without changing the deposit workflow. Code generation has its own strategy for the same reason.

Pickup checks the customer's role, the compartment, and the code. It then clears the allocation and returns the package. `expirePackagesFromContainers()` separately scans occupied compartments and releases expired packages.

Observers keep notification and audit handling outside allocation logic. `AuditObserver` returns copied event snapshots. `EmailSubscriber` filters events by customer ID and accepts an injectable sender. Listener failures are caught so another subscriber can still receive the event.

## Concurrency and trade-offs

Locker operations are synchronized on the instance. This keeps compound allocation changes together, but listeners also run within that operation, so a slow sender delays other locker requests. Allocation and expiry cleanup both take O(n) time in the number of compartments they inspect.

## Current boundaries

- Pickup does not verify that the caller is the recorded recipient, and it does not check expiry directly. An expired code remains usable until cleanup runs.
- Cleanup must be invoked by the caller; there is no background expiry worker.
- Random codes are not guaranteed unique or generated with a security-focused random source.
- Deposit events do not include the compartment code. The demo knows its compartment labels in advance.
- The older deposit overload associates the agent's ID with the package; use the explicit-recipient overload for the demonstrated customer flow.

## Try it

Run [`amazon_locker.Main`](Main.java). It covers size fallback, a full station, an incorrect code, successful pickup, reuse of a freed compartment, and audit history. Its cleanup call uses fresh packages, so it does not demonstrate an actually expired package.

For an interview discussion, the next useful step is enforcing recipient and expiry checks at pickup, then deciding how to handle notification failures without holding the locker lock.
