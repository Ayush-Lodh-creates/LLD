package vending_machine.interfaces;

import vending_machine.VendingMachine;
import vending_machine.entities.Product;
import vending_machine.entities.Inventory;
import vending_machine.entities.Slot;

public class DispenseState implements VendingMachineState {

    @Override
    public void showItems(VendingMachine vendingMachine) {
        System.out.println("Items are being dispensed. Cannot show items now.");
    }

    @Override
    public void addMoney(VendingMachine vendingMachine, int amount) {
        System.out.println("Items are being dispensed. Cannot add money now.");
    }

    @Override
    public void selectProduct(VendingMachine vendingMachine, String slotCode) {
        System.out.println("Items are being dispensed. Cannot select product now.");
    }

    @Override
    public void dispense(VendingMachine vendingMachine) {
        try {
            Inventory inventory = vendingMachine.getInventory();
            String slotCode = vendingMachine.getSlotCode();
            Slot slot = inventory.getSlotDetails(slotCode);
            if (slot == null) {
                throw new IllegalArgumentException("Invalid slot code: " + slotCode);
            }
            int cost = slot.getProduct().getCost();
            if (vendingMachine.getAmount() < cost) {
                throw new IllegalStateException("Insufficient funds for the product: "
                        + slot.getProduct().getProductName());
            }
            Product product = inventory.removeItem(slotCode);
            vendingMachine.setAmount(vendingMachine.getAmount() - cost);
            System.out.println("Dispensed product : " + product.getProductName());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error during dispensing: " + e.getMessage());
        } finally {
            System.out.println("Returning balance amount : " + vendingMachine.getAmount());
            vendingMachine.setAmount(0);
            vendingMachine.setSlotCode(null);
            vendingMachine.setCurrentState(new IdleState());
        }
    }

    @Override
    public void cancel(VendingMachine vendingMachine) {
        System.out.println("Money refunded : " + vendingMachine.getAmount());
        vendingMachine.setAmount(0);
        vendingMachine.setSlotCode(null);
        vendingMachine.setCurrentState(new IdleState());
    }
}
