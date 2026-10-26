package vending_machine.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Inventory {

    private static Inventory instance;
    private final Map<String, Slot> slots = new HashMap<>();
    private int amountCollected;

    private Inventory() {
        amountCollected = 0;
    }

    public static Inventory getInstance() {
        if(instance == null) {
            instance = new Inventory();
        }
        return instance;
    }

    public void addProducts(String productName, String slotCode, int cost, int quantity) {
        String productId = UUID.randomUUID().toString();
        Product product = new Product(productId, productName, cost);
        String slotId = UUID.randomUUID().toString();
        Slot slot = new Slot(slotId, slotCode, quantity, product);
        slots.put(slotCode, slot);
    }

    public void showInventory() {
        for(Map.Entry<String, Slot> entry : slots.entrySet()) {
            System.out.println("Slot Code: " + entry.getKey() + ", Product: " + entry.getValue().getProduct().getProductName()
                    + ", Cost: " + entry.getValue().getProduct().getCost()
                    + ", Quantity: " + entry.getValue().getQuantity());
        }
    }

    public Product removeItem(String slotCode) {
        Slot slot = slots.get(slotCode);
        if(slot == null) {
            throw new IllegalArgumentException("Invalid slot code: " + slotCode);
        }
        if(slot.getQuantity() <= 0) {
            throw new IllegalStateException("Product is out of stock for slot code: " + slotCode);
        }
        amountCollected += slot.getProduct().getCost();
        slot.setQuantity(slot.getQuantity() - 1);
        return slot.getProduct();
    }

    public boolean checkSlotExists(String slotCode) {
        return slots.containsKey(slotCode);
    }

    public Slot getSlotDetails(String slotCode) {
        if(checkSlotExists(slotCode)) {
            return slots.get(slotCode);
        }
        return null;
    }
}
