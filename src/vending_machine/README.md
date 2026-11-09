# Vending Machine

This example models one purchase session: show products, accept money, select a slot, and either dispense or cancel. The key design choice is to let the current state decide which actions are valid.

## Objects and responsibilities

[`VendingMachine`](VendingMachine.java) holds the current state, inserted balance, and selected slot code. Its public actions delegate to `VendingMachineState`.

[`Inventory`](entities/Inventory.java) is a singleton map of slot codes to `Slot` objects. A slot contains a product and quantity; a product has a name and integer cost. Removing an item reduces stock and adds its price to the internal amount-collected counter.

## State flow

```text
IdleState --addMoney--> HasMoneyState --select valid slot--> DispenseState
   ^                        |                                  |
   |                        +--cancel / invalid slot-----------+
   +---------------------------cancel / dispense---------------+
```

In `IdleState`, product display and a positive deposit are allowed. Selecting or dispensing before inserting money only prints an explanatory message.

`HasMoneyState` accepts additional deposits and allows product selection. Deposits must be positive, and accumulated balance uses `Math.addExact()` to detect integer overflow. A valid slot moves the machine to `DispenseState`; an invalid slot refunds the balance and returns to idle.

Selection checks that the slot exists. Stock and sufficient funds are checked later, inside `dispense()`. Once selected, more money and another selection are not accepted in that state.

On successful dispense, inventory removes one product and the price is deducted from the balance. A `finally` block returns the remaining balance, clears the selection, and resets the machine to idle. That same cleanup runs for insufficient funds or out-of-stock failures, returning the full inserted balance. Cancellation before dispensing also refunds the balance without changing stock.

## Why State fits here

The meaning of an action depends on the transaction stage. Keeping this behavior in `IdleState`, `HasMoneyState`, and `DispenseState` avoids repeating state checks across the machine's public methods. The state classes live in the `interfaces` folder alongside the interface.

## Current boundaries

- Money, refunds, and dispensing are simulated with console messages. There is no denomination inventory or physical change calculation.
- Inventory is shared across all machine instances. It is not an independent inventory per physical machine.
- Neither the transaction nor singleton initialization is protected for concurrent use.
- `addProducts()` replaces an existing slot with the same code; it is not an incremental restock operation.
- Costs and quantities reject negative values, but a complete administrative validation layer is not implemented.

## Try it

Run [`vending_machine.Main`](Main.java). It demonstrates a purchase with change, cancellation after selection, insufficient funds, and an out-of-stock product, printing inventory before and after.

For an interview extension, decide whether insufficient funds should retain the session so the customer can add money. That changes the state flow and refund policy, rather than just adding another validation check.
