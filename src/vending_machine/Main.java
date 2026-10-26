package vending_machine;

import vending_machine.entities.Inventory;

public class Main {

    public static void main(String[] args) {
        Inventory inventory = Inventory.getInstance();
        inventory.addProducts("Chips", "A1", 20, 2);
        inventory.addProducts("Soda", "A2", 30, 3);
        inventory.addProducts("Chocolate", "A3", 25, 0);

        VendingMachine machine = new VendingMachine();

        System.out.println("\n--- Available products ---");
        machine.showItems();

        // Buy Chips for 20 using two deposits totaling 30; receive 10 back.
        System.out.println("\n--- Purchase with change ---");
        machine.addMoney(10);
        machine.addMoney(20);
        machine.selectProduct("A1");
        machine.dispense();

        System.out.println("\n--- Inventory after purchase ---");
        machine.showItems();

        // Cancel before dispensing; stock remains unchanged.
        System.out.println("\n--- Cancel a purchase ---");
        machine.addMoney(30);
        machine.selectProduct("A2");
        machine.cancel();

        // The current implementation refunds the balance on a failed dispense.
        System.out.println("\n--- Insufficient funds ---");
        machine.addMoney(10);
        machine.selectProduct("A2");
        machine.dispense();

        System.out.println("\n--- Out of stock ---");
        machine.addMoney(25);
        machine.selectProduct("A3");
        machine.dispense();

        System.out.println("\n--- Final inventory ---");
        machine.showItems();
    }
}
