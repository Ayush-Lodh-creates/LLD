package amazon_locker.factory;

import amazon_locker.entities.Container;
import amazon_locker.enums.ContainerSize;

import java.util.UUID;

public class ContainerFactory {

    public static Container createContainer(String code, ContainerSize size) {
        switch(size) {
            case SMALL -> {
                return new Container(UUID.randomUUID().toString(), code, ContainerSize.SMALL);
            }
            case MEDIUM -> {
                return new Container(UUID.randomUUID().toString(), code, ContainerSize.MEDIUM);
            }
            case LARGE -> {
                return new Container(UUID.randomUUID().toString(), code, ContainerSize.LARGE);
            }
        }
        return null;
    }
}
