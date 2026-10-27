package amazon_locker.observer;

import amazon_locker.entities.Event;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** In-memory audit history, in event delivery order. */
public class AuditObserver implements LockerEventListener {
    private final List<Event> events = new ArrayList<>();

    @Override
    public synchronized void onEvent(Event event) {
        events.add(copy(event));
    }

    public synchronized List<Event> getEvents() {
        List<Event> snapshot = new ArrayList<>();
        for (Event event : events) snapshot.add(copy(event));
        return Collections.unmodifiableList(snapshot);
    }

    private Event copy(Event event) {
        Event copy = new Event(event.getCustomerId(), event.getProductName(), event.getAccessCode(), event.getEventType());
        copy.setDetails(event.getDetails());
        return copy;
    }
}
