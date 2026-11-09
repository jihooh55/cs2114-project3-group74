package project;
import java.util.NoSuchElementException;

public class SongQueue<E>{
    Node<E> head;
    Node<E> tail;
    int numberOfEntries;
    
    public SongQueue() {
        head = null;
        tail = null;
        numberOfEntries = 0;
    }
    
    public void enqueue(E newEntry) {
        Node<E> newNode = new Node<>(newEntry);
        if (head == null) {
            head = newNode;
            tail = newNode;
        }
        else {
            tail.setNext(newNode);
            tail = newNode;
        }
        numberOfEntries ++;
    }
    
    public Node<E> dequeue() {
        Node<E> removed;
        if (numberOfEntries == 0) {
            throw new NoSuchElementException();
        }
        if (numberOfEntries == 1) {
            tail = null;
        }
        removed = head;
        head = head.getNext();
        numberOfEntries --;
        return removed;
    }
    
    public int size() {
        return numberOfEntries;
    }
    
    public Node<E> getFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head;
    }
    //~ Fields ................................................................

    //~ Constructors ..........................................................

    //~Public  Methods ........................................................

}
