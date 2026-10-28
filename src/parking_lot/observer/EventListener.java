package parking_lot.observer;

import parking_lot.entities.Event;

public interface EventListener {

    public void onEvent(Event event);
}
