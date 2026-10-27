package amazon_locker;

import amazon_locker.entities.Event;
import amazon_locker.entities.Locker;
import amazon_locker.entities.Product;
import amazon_locker.entities.User;
import amazon_locker.enums.EventType;
import amazon_locker.enums.ProductSize;
import amazon_locker.enums.UserType;
import amazon_locker.observer.AuditObserver;
import amazon_locker.observer.EmailSubscriber;
import amazon_locker.strategy.AccessCodeGeneratorStrategy;
import amazon_locker.strategy.ContainerSelectionStrategy;
import amazon_locker.strategy.GreedyContainerStrategy;
import amazon_locker.strategy.RandomAccessCodeGeneratorStrategy;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        User agent = new User("AGENT-1", "Delivery Agent", "agent@example.com", UserType.DELIVERY_AGENT);
        User customer = new User("CUSTOMER-1", "Ayush", "ayush@example.com", UserType.CUSTOMER);

        AuditObserver audit = new AuditObserver();
        Locker locker = new Locker(1, 1, 1, new EmailSubscriber(customer), audit);
        ContainerSelectionStrategy selection = new GreedyContainerStrategy();
        AccessCodeGeneratorStrategy generator = new RandomAccessCodeGeneratorStrategy();

        System.out.println("\n--- Deposit: smallest available compartment ---");
        Product book = new Product("P1", "Book", ProductSize.SMALL);
        locker.addPackagesToContainers(selection, generator, book, agent, customer);
        int bookCode = latestDepositCode(audit);
        // This demo station has one compartment per size, named S1, M1, and L1.
        // Until deposit events include the compartment code, the demo uses these known labels.

        System.out.println("\n--- Small package falls back to medium ---");
        Product headphones = new Product("P2", "Headphones", ProductSize.SMALL);
        locker.addPackagesToContainers(selection, generator, headphones, agent, customer);
        int headphonesCode = latestDepositCode(audit);

        System.out.println("\n--- Deposit a large package ---");
        Product monitor = new Product("P3", "Monitor", ProductSize.LARGE);
        locker.addPackagesToContainers(selection, generator, monitor, agent, customer);
        int monitorCode = latestDepositCode(audit);

        System.out.println("\n--- No compartment available ---");
        expectRejection(() -> locker.addPackagesToContainers(selection, generator,
                new Product("P4", "Keyboard", ProductSize.SMALL), agent, customer));

        System.out.println("\n--- Invalid access code ---");
        // Generated codes are four digits, so -1 cannot accidentally match.
        expectRejection(() -> locker.pickPackagesFromContainers("S1", -1, customer));

        System.out.println("\n--- Successful pickup ---");
        Product collected = locker.pickPackagesFromContainers("S1", bookCode, customer);
        System.out.println("Collected: " + collected.getProductName());

        System.out.println("\n--- Used code cannot collect again ---");
        expectRejection(() -> locker.pickPackagesFromContainers("S1", bookCode, customer));

        System.out.println("\n--- Freed compartment can be allocated again ---");
        locker.addPackagesToContainers(selection, generator,
                new Product("P5", "Charger", ProductSize.SMALL), agent, customer);
        int chargerCode = latestDepositCode(audit);

        System.out.println("\n--- Expiry cleanup: newly deposited packages are still valid ---");
        List<Product> expired = locker.expirePackagesFromContainers();
        System.out.println("Expired packages released: " + expired.size());

        System.out.println("\n--- Collect remaining packages ---");
        System.out.println("Collected: " + locker.pickPackagesFromContainers("S1", chargerCode, customer).getProductName());
        System.out.println("Collected: " + locker.pickPackagesFromContainers("M1", headphonesCode, customer).getProductName());
        System.out.println("Collected: " + locker.pickPackagesFromContainers("L1", monitorCode, customer).getProductName());

        System.out.println("\n--- Audit history ---");
        for (Event event : audit.getEvents()) {
            System.out.println(event.getEventType() + " | customer=" + event.getCustomerId()
                    + " | product=" + event.getProductName());
        }
    }

    private static int latestDepositCode(AuditObserver audit) {
        List<Event> events = audit.getEvents();
        for (int i = events.size() - 1; i >= 0; i--) {
            if (events.get(i).getEventType() == EventType.PACKAGE_DEPOSITED) {
                return events.get(i).getAccessCode();
            }
        }
        throw new IllegalStateException("No deposit event was recorded");
    }

    private static void expectRejection(Runnable operation) {
        try {
            operation.run();
        } catch (IllegalArgumentException expected) {
            System.out.println("Expected rejection: " + expected.getMessage());
            return;
        }
        throw new IllegalStateException("Expected the operation to be rejected");
    }
}
