package amazon_locker.strategy;

import amazon_locker.entities.Container;
import amazon_locker.enums.ContainerSize;
import amazon_locker.enums.ProductSize;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** Selects the smallest available container that can fit the product. */
public class GreedyContainerStrategy implements ContainerSelectionStrategy {
    @Override
    public Container selectContainer(Map<ContainerSize, List<Container>> containers, ProductSize productSize) {
        ContainerSize minimum = ContainerSize.valueOf(productSize.name());
        for (ContainerSize size : ContainerSize.values()) {
            if (size.ordinal() < minimum.ordinal()) continue;
            for (Container container : containers.getOrDefault(size, Collections.emptyList())) {
                if (!Boolean.TRUE.equals(container.getBooked())) return container;
            }
        }
        return null;
    }
}
