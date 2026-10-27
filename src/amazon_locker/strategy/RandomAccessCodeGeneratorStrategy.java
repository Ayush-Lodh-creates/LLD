package amazon_locker.strategy;

public class RandomAccessCodeGeneratorStrategy implements AccessCodeGeneratorStrategy {
    @Override
    public Integer generateCode() {
        return 1000 + (int) (Math.random() * 9000);
    }
}
