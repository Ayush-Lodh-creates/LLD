package amazon_locker.entities;

import amazon_locker.enums.ContainerSize;
import amazon_locker.enums.EventType;
import amazon_locker.enums.UserType;
import amazon_locker.factory.ContainerFactory;
import amazon_locker.observer.LockerEventListener;
import amazon_locker.strategy.AccessCodeGeneratorStrategy;
import amazon_locker.strategy.ContainerSelectionStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Locker {

    private Map<ContainerSize, List<Container>> containers;
    private List<LockerEventListener> eventListeners;
    private Map<String, Container> bookedContainersMap;

    private Map<String, String> customerIds = new ConcurrentHashMap<>();

    public Locker(int small, int medium, int large) {
        this(small, medium, large, new LockerEventListener[0]);
    }

    public Locker(int small, int medium, int large, LockerEventListener... listeners) {
        containers = new ConcurrentHashMap<>();
        eventListeners = new CopyOnWriteArrayList<>();
        bookedContainersMap = new ConcurrentHashMap<>();
        for (LockerEventListener listener : listeners) addEventListeners(listener);
        initializeContainers(small, medium, large);
    }

    public synchronized void addEventListeners(LockerEventListener eventListener) {
        eventListeners.add(eventListener);
    }

    public synchronized void removeEventListeners(LockerEventListener eventListener) {
        eventListeners.remove(eventListener);
    }

    private void initializeContainers(int small, int medium, int large) {
        List<Container> smallContainers = new ArrayList<>();
        for(int i=1; i<=small; i++) {
            smallContainers.add(ContainerFactory.createContainer("S" + i, ContainerSize.SMALL));
        }
        containers.put(ContainerSize.SMALL, smallContainers);

        List<Container> mediumContainers = new ArrayList<>();
        for(int i=1; i<=medium; i++) {
            mediumContainers.add(ContainerFactory.createContainer("M" + i, ContainerSize.MEDIUM));
        }
        containers.put(ContainerSize.MEDIUM, mediumContainers);

        List<Container> largeContainers = new ArrayList<>();
        for(int i=1; i<=large; i++) {
            largeContainers.add(ContainerFactory.createContainer("L" + i, ContainerSize.LARGE));
        }
        containers.put(ContainerSize.LARGE, largeContainers);
        publishAuditEvent(EventType.LOCKER_INITIALISED,
                "small=" + small + ", medium=" + medium + ", large=" + large);
    }

    public synchronized void addContainers(ContainerSize size) {
        List<Container> containerList = containers.get(size);
        String code = "";
        if(size == ContainerSize.SMALL) {
            code = "S";
        } else if(size == ContainerSize.MEDIUM) {
            code = "M";
        } else {
            code = "L";
        }
        Container container = ContainerFactory.createContainer(code + (containerList.size()+1), size);
        containerList.add(container);
        publishAuditEvent(EventType.CONTAINER_ADDED, container.getContainerCode() + " (" + size + ")");
    }

    public synchronized void removeContainers(ContainerSize size) {
        List<Container> containerList = containers.get(size);
        int containerIndexToRemove = -1;
        for(int i=containerList.size()-1; i>=0; i--) {
            if(!containerList.get(i).getBooked()) {
                containerIndexToRemove = i;
                break;
            }
        }
        if(containerIndexToRemove != -1) {
            System.out.println("Container with code : " + containerList.get(containerIndexToRemove).getContainerCode() + " is removed");
            Container removed = containerList.remove(containerIndexToRemove);
            publishAuditEvent(EventType.CONTAINER_REMOVED, removed.getContainerCode() + " (" + size + ")");
        } else {
            System.out.println("All Containers are booked cannot delete any container");
        }
    }

    public synchronized void addPackagesToContainers(ContainerSelectionStrategy containerSelectionStrategy, AccessCodeGeneratorStrategy accessCodeGeneratorStrategy, Product product, User user) {
        depositPackage(containerSelectionStrategy, accessCodeGeneratorStrategy, product, user, user.getId());
    }

    public synchronized void addPackagesToContainers(ContainerSelectionStrategy selection,
            AccessCodeGeneratorStrategy generator, Product product, User deliveryAgent, User customer) {
        if (customer.getUserType() != UserType.CUSTOMER) {
            throw new IllegalArgumentException("Package recipient must be a customer");
        }
        depositPackage(selection, generator, product, deliveryAgent, customer.getId());
    }

    private void depositPackage(ContainerSelectionStrategy containerSelectionStrategy,
            AccessCodeGeneratorStrategy accessCodeGeneratorStrategy, Product product, User user, String customerId) {
        if(user.getUserType() != UserType.DELIVERY_AGENT) {
            throw new IllegalArgumentException("Only Delivery Agent can add packages to containers");
        }
        Container container = containerSelectionStrategy.selectContainer(containers, product.getProductSize());
        if(container == null) {
            throw new IllegalArgumentException("No available container for the product size: " + product.getProductSize());
        }
        container.setProduct(product);
        container.setBooked(true);
        Integer code = accessCodeGeneratorStrategy.generateCode();
        AccessCode accessCode = new AccessCode(code, System.currentTimeMillis(), System.currentTimeMillis() + 7*24*60*60*1000);
        container.setAccessCode(accessCode);
        System.out.println("Package " + product.getProductName() + " is added to container with code: " + container.getContainerCode());
        bookedContainersMap.put(container.getContainerCode(), container);
        customerIds.put(container.getContainerCode(), customerId);
        Event event = new Event(customerId, product.getProductName(), code, EventType.PACKAGE_DEPOSITED);
        publishEvent(event);
    }

    public synchronized Product pickPackagesFromContainers(String containerCode, Integer accessCode, User user) {
        if(user.getUserType() != UserType.CUSTOMER) {
            throw new IllegalArgumentException("Only Customer can pick packages from containers");
        }
        if(!bookedContainersMap.containsKey(containerCode)) {
            throw new IllegalArgumentException("Invalid container code: " + containerCode);
        }
        Container container = bookedContainersMap.get(containerCode);
        if(!container.getAccessCode().getCode().equals(accessCode)) {
            throw new IllegalArgumentException("Invalid access code for container code: " + containerCode);
        }
        Product product = container.getProduct();
        container.setBooked(false);
        container.setProduct(null);
        container.setAccessCode(null);
        bookedContainersMap.remove(containerCode);
        customerIds.remove(containerCode);
        Event event = new Event(user.getId(), product.getProductName(), accessCode, EventType.PACKAGE_PICKED_UP);
        publishEvent(event);
        return product;
    }

    public synchronized List<Product> expirePackagesFromContainers() {
        List<Product> expiredPackages = new ArrayList<>();
        for(Map.Entry<String, Container> entry : bookedContainersMap.entrySet()) {
            Container container = entry.getValue();
            if(container.getAccessCode().getExpiresOn() < System.currentTimeMillis()) {
                Product product = container.getProduct();
                container.setBooked(false);
                container.setProduct(null);
                container.setAccessCode(null);
                bookedContainersMap.remove(entry.getKey());
                String customerId = customerIds.remove(entry.getKey());
                Event event = new Event(customerId, product.getProductName(), null, EventType.PACKAGE_EXPIRED);
                publishEvent(event);
                expiredPackages.add(product);
            }
        }
        return expiredPackages;
    }
    private void publishAuditEvent(EventType type, String details) {
        Event event = new Event("SYSTEM", null, null, type);
        event.setDetails(details);
        publishEvent(event);
    }

    private void publishEvent(Event event) {
        for (LockerEventListener listener : eventListeners) {
            try {
                listener.onEvent(event);
            } catch (RuntimeException exception) {
                // One failed subscriber must not prevent the others receiving the event.
                System.err.println("Locker listener failed: " + exception.getMessage());
            }
        }
    }
}
