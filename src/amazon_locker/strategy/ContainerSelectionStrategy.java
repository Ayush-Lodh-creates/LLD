package amazon_locker.strategy;

import amazon_locker.entities.Container;
import amazon_locker.enums.ContainerSize;
import amazon_locker.enums.ProductSize;

import java.util.List;
import java.util.Map;

public interface ContainerSelectionStrategy {

    public Container selectContainer(Map<ContainerSize, List<Container>> containers, ProductSize productSize);
}
