package notification_system;

import notification_system.entities.Client;
import notification_system.entities.notification.Notification;
import notification_system.entities.subscriber.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class NotificationService {

    private static NotificationService notificationService = null;

    Map<String, List<Notification>> clientNotificationMap;
    Map<String, List<Subscriber>> clientSubscriberMap;
    Map<String, ReentrantLock> clientLockMap;

    private NotificationService() {
        clientNotificationMap = new ConcurrentHashMap<>();
        clientSubscriberMap = new ConcurrentHashMap<>();
        clientLockMap = new ConcurrentHashMap<>();
    }

    public static NotificationService getInstance() {
        if (notificationService == null) {
            notificationService = new NotificationService();
        }
        return notificationService;
    }

    public void onBoardClient(Client client, List<Subscriber> subscriberList) {
        ReentrantLock lock;
        if(clientLockMap.containsKey(client.getClientId())) {
            lock = clientLockMap.get(client.getClientId());
        } else {
            lock = new ReentrantLock();
            clientLockMap.put(client.getClientId(), lock);
        }
        lock.lock();
        clientSubscriberMap.put(client.getClientId(), subscriberList);
        lock.unlock();
    }

    // Skipping addSubscriber and removeSubscriber due to interviewer asking to consider them as implemented

    public void processNotification(Notification notification) {
        String clientId = notification.getClientId();
        if(!clientLockMap.containsKey(clientId) || !clientSubscriberMap.containsKey(clientId)) {
            throw new IllegalArgumentException("Client not onboarded");
        }
        ReentrantLock lock = clientLockMap.get(clientId);
        lock.lock();
        clientNotificationMap.computeIfAbsent(clientId, key -> new ArrayList<>()).add(notification);
        List<Subscriber> subscriberList = clientSubscriberMap.get(clientId);
        for(Subscriber subscriber : subscriberList) {
            subscriber.onEvent(notification);
        }
        lock.unlock();
    }
}
