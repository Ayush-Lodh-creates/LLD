package amazon_locker.observer;

import amazon_locker.entities.Event;
import amazon_locker.entities.User;
import amazon_locker.enums.EventType;
import java.util.Objects;
import java.util.function.BiConsumer;

/** One subscriber per customer. Inject a sender to integrate an email service. */
public class EmailSubscriber implements LockerEventListener {
    private final User user;
    private final BiConsumer<String, String> sender;

    public EmailSubscriber(User user) {
        this(user, (email, body) -> System.out.println("Email to " + email + ": " + body));
    }

    public EmailSubscriber(User user, BiConsumer<String, String> sender) {
        this.user = Objects.requireNonNull(user);
        this.sender = Objects.requireNonNull(sender);
    }

    @Override
    public void onEvent(Event event) {
        if (!Objects.equals(user.getId(), event.getCustomerId())) return;
        if (event.getEventType() == EventType.PACKAGE_DEPOSITED) {
            sender.accept(user.getEmail(), "Your package " + event.getProductName()
                    + " has been deposited. Access code: " + event.getAccessCode());
        } else if (event.getEventType() == EventType.PACKAGE_EXPIRED) {
            sender.accept(user.getEmail(), "The collection period for your package "
                    + event.getProductName() + " has expired.");
        }
    }
}
