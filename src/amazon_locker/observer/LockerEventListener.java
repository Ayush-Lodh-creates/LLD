package amazon_locker.observer;

import amazon_locker.entities.Event;

public interface LockerEventListener {

    public void onEvent(Event event);
}
