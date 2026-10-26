package vending_machine.interfaces;

import vending_machine.VendingMachine;

public class HasMoneyState implements VendingMachineState {

    @Override
    public void showItems(VendingMachine vendingMachine) {
        vendingMachine.showProducts();
    }

    @Override
    public void addMoney(VendingMachine vendingMachine, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Inserted amount must be positive");
        }
        Integer currentAmount = vendingMachine.getAmount();
        currentAmount = Math.addExact(currentAmount, amount);
        System.out.println("You have already added : " + currentAmount);
        vendingMachine.setAmount(currentAmount);
    }

    @Override
    public void selectProduct(VendingMachine vendingMachine, String slotCode) {
        if(vendingMachine.validateSlotCode(slotCode)) {
            vendingMachine.setSlotCode(slotCode);
            System.out.println("You have selected the slot : " + slotCode);
            vendingMachine.setCurrentState(new DispenseState());
        } else {
            System.out.println("Invalid slot selected, Money refunded : " + vendingMachine.getAmount());
            vendingMachine.setAmount(0);
            vendingMachine.setCurrentState(new IdleState());
        }
    }

    @Override
    public void dispense(VendingMachine vendingMachine) {
        System.out.println("Please select a product first");
    }

    @Override
    public void cancel(VendingMachine vendingMachine) {
        System.out.println("Money refunded : " + vendingMachine.getAmount());
        vendingMachine.setAmount(0);
        vendingMachine.setCurrentState(new IdleState());
    }
}
