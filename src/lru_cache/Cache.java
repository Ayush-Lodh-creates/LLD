package lru_cache;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    private final Map<Integer, Node> keyNodeMap;
    private Node head;
    private Node tail;
    private Integer totalSize;
    private Long validity;

    public Cache(int size, long validity) {
        if (size <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        if (validity <= 0) {
            throw new IllegalArgumentException("TTL must be positive");
        }
        keyNodeMap = new HashMap<>();
        head = new Node(-1, -1, -1, null, null);
        tail = new Node(-1, -1, -1, null, null);
        head.setNext(tail);
        tail.setPrev(head);
        totalSize = size;
        this.validity = validity;
    }

    public synchronized void put(int key, int value) {
        evict();
        if(keyNodeMap.containsKey(key)) {
            Node node = keyNodeMap.get(key);
            node.setValue(value);
            node.setValidUntil(System.currentTimeMillis() + validity);
            Node prev = node.getPrev();
            Node next = node.getNext();
            prev.setNext(next);
            next.setPrev(prev);
            Node headNext = head.getNext();
            head.setNext(node);
            node.setPrev(head);
            node.setNext(headNext);
            headNext.setPrev(node);
        } else {
            if(keyNodeMap.size() == totalSize) {
                Node prevTail = tail.getPrev();
                Node prevPrevTail = prevTail.getPrev();
                prevPrevTail.setNext(tail);
                tail.setPrev(prevPrevTail);
                keyNodeMap.remove(prevTail.getKey());
                prevTail = null;
                Node newNode = new Node(key, value, System.currentTimeMillis() + validity, null, null);
                keyNodeMap.put(key, newNode);
                Node headNext = head.getNext();
                head.setNext(newNode);
                newNode.setPrev(head);
                newNode.setNext(headNext);
                headNext.setPrev(newNode);
            } else {
                Node newNode = new Node(key, value, System.currentTimeMillis() + validity, null, null);
                keyNodeMap.put(key, newNode);
                Node headNext = head.getNext();
                head.setNext(newNode);
                newNode.setPrev(head);
                newNode.setNext(headNext);
                headNext.setPrev(newNode);
            }
        }
    }

    public synchronized Integer get(int key) {
        evict();
        if(keyNodeMap.containsKey(key)) {
            Node node = keyNodeMap.get(key);
            Node prev = node.getPrev();
            Node next = node.getNext();
            prev.setNext(next);
            next.setPrev(prev);
            Node headNext = head.getNext();
            head.setNext(node);
            node.setPrev(head);
            node.setNext(headNext);
            headNext.setPrev(node);
            return node.getValue();
        }
        return null;
    }

    private void evict() {
        for(Map.Entry<Integer, Node> entry : keyNodeMap.entrySet()) {
            Node node = entry.getValue();
            if(System.currentTimeMillis() > node.getValidUntil()) {
                Node prev = node.getPrev();
                Node next = node.getNext();
                prev.setNext(next);
                next.setPrev(prev);
                keyNodeMap.remove(node.getKey());
            }
        }
    }
}
