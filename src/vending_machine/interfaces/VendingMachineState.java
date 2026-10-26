package vending_machine.interfaces;

import vending_machine.VendingMachine;

public interface VendingMachineState {

    void showItems(VendingMachine vendingMachine);
    void addMoney(VendingMachine vendingMachine, int amount);
    void selectProduct(VendingMachine vendingMachine, String slotCode);
    void dispense(VendingMachine vendingMachine);
    void cancel(VendingMachine vendingMachine);
}
