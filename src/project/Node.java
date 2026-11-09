package project;

public class Node<E> {
    E data;
    Node<E> next;
    
    public Node(E data) {
        this.data = data;
        this.next = null;
    }
    
    public Node(E data, Node<E> next) {
        this.data = data;
        this.next = next;
    }
    
    public void setNext(Node<E> newNode) {
        this.next = newNode;
    }
    
    public Node<E> getNext() {
        return this.next;
    }
    
    public E getData() {
        return this.data;
    }

}
