package lru_cache;

public class Node {

    private int key;
    private int value;
    private long validUntil;
    private Node prev;
    private Node next;

    public Node(int key, int value, long validUntil, Node prev, Node next) {
        this.key = key;
        this.value = value;
        this.validUntil = validUntil;
        this.prev = prev;
        this.next = next;
    }

    public int getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(long validUntil) {
        this.validUntil = validUntil;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
