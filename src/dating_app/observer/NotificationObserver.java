package dating_app.observer;

import dating_app.entities.Message;

public abstract class NotificationObserver {
    public abstract void update(String message);
}

