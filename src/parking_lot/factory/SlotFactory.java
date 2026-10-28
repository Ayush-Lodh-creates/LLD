package parking_lot.factory;

import parking_lot.entities.Slot;
import parking_lot.enums.SlotType;

public class SlotFactory {

    public static Slot createSlot(String slotCode, SlotType slotType) {
        return new Slot(slotCode, slotType);
    }
}
