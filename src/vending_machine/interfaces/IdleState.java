package vending_machine.interfaces;

import vending_machine.VendingMachine;

public class IdleState implements VendingMachineState {

    @Override
    public void showItems(VendingMachine vendingMachine) {
        vendingMachine.showProducts();
    }

    @Override
    public void addMoney(VendingMachine vendingMachine, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Inserted amount must be positive");
        }
        vendingMachine.setAmount(amount);
        System.out.println("Amount inserted : " + amount);
        vendingMachine.setCurrentState(new HasMoneyState());
    }

    @Override
    public void selectProduct(VendingMachine vendingMachine, String slotCode) {
        System.out.println("Please add amount first.");
    }

    @Override
    public void dispense(VendingMachine vendingMachine) {
        System.out.println("Please select a product first.");
    }

    @Override
    public void cancel(VendingMachine vendingMachine) {
        System.out.println("No transaction to cancel.");
    }
}
