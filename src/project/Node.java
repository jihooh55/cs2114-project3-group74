package project;

/**
 * Creates a new Node of type E
 * 
 * @param <E>
 *            Generic type that will be used to create a Node object
 * @author
 * @version Nov 12, 2026
 */
public class Node<E> {
    /**
     * Each node has data field
     */
    E data;
    /**
     * and next field that contains the address of the Node after
     */
    Node<E> next;

    /**
     * Construction method for a Node
     * 
     * @param data
     *            Data of type E that the Node will hold
     */
    public Node(E data) {
        this.data = data;
        this.next = null;
    }


    /**
     * Alternative construction method for a Node
     * 
     * @param data
     *            Data of type E that the Node will hold
     * @param next
     *            The address of the Node after
     */
    public Node(E data, Node<E> next) {
        this.data = data;
        this.next = next;
    }


    /**
     * Method to set the Node's next field
     * 
     * @param newNode
     *            The address of the Node this Node object's next will hold
     */
    public void setNext(Node<E> newNode) {
        this.next = newNode;
    }


    /**
     * Method to return the Node's next field
     * 
     * @return Node<E>
     *         The Node object this Node object's next field points to
     */
    public Node<E> getNext() {
        return this.next;
    }


    /**
     * Method to return the Node's data
     * 
     * @return E
     *         The data of type E this Node holds in its data field
     */
    public E getData() {
        return this.data;
    }

}
