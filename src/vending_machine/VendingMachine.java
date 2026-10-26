package vending_machine;

import vending_machine.entities.Inventory;
import vending_machine.interfaces.IdleState;
import vending_machine.interfaces.VendingMachineState;

public class VendingMachine {

    private VendingMachineState currentState;
    private final Inventory inventory;
    private Integer amount;
    private String slotCode;

    public VendingMachine() {
        currentState = new IdleState();
        inventory = Inventory.getInstance();
        amount = 0;
        slotCode = null;
    }

    public void showProducts() {
        inventory.showInventory();
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("Balance must be nonnegative");
        }
        this.amount = amount;
    }

    public void setSlotCode(String slotCode) {
        this.slotCode = slotCode;
    }

    public void setCurrentState(VendingMachineState currentState) {
        this.currentState = currentState;
    }

    public boolean validateSlotCode(String code) {
        return inventory.checkSlotExists(code);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getSlotCode() {
        return slotCode;
    }

    public void showItems() {
        currentState.showItems(this);
    }

    public void addMoney(int amount) {
        currentState.addMoney(this, amount);
    }

    public void selectProduct(String slotCode) {
        currentState.selectProduct(this, slotCode);
    }

    public void cancel() {
        currentState.cancel(this);
    }

    public void dispense() {
        currentState.dispense(this);
    }
}
