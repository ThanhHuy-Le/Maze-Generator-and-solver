/**
 * @author Heon Jae Jeong
 * Summer 2018
 * Lab 1
 */
public class SinglyLinkedList<E> { 
	private static class Node<E> { //Nested Node class extends Comparable
		/** The element stored at this node */
		private E element;
		/** A reference to the subsequent node in the list */
		private Node<E> next;
		/**
	     * Creates a node with the given element and next node.
	     *
	     * @param e  the element to be stored
	     * @param n  reference to a node that should follow the new node
	     */
		public Node(E e, Node<E> n) {
			this.element = e;
			this.next = n;
		}
		// Accessor methods
	    /**
	     * Returns the element stored at the node.
	     * @return the element stored at the node
	     */
		public E getElement() {
			return element;
		}
		/**
	     * Returns the node that follows this one (or null if no such node).
	     * @return the following node
	     */
		public Node<E> getNext() {
			return next;
		}
		// Modifier methods
	    /**
	     * Sets the node's next reference to point to Node n.
	     * @param n    the node that should follow this one
	     */
		public void setNext(Node<E> n) {
			next = n;
		}
		/**
		 * return toString of the node
		 */
		public String toString() {
			return "Element: " + this.element;
		}
	}
	//Instance variables
	 /** The head node of the list */
	private Node<E> head;
	/** The last node of the list */
	private Node<E> tail;
	/** Number of nodes in the list */
	private int size;

	/**
	 * Default Constructor
	 */
	public SinglyLinkedList() { 
		head = null;
		tail = null;
		size = 0;
	}

	/**
	 * @param maxSize int
	 * maxSize indicates the largest size this linkedlist should be
	 */
	public SinglyLinkedList(int maxSize) { 
		head = null;
		tail = null;
		size = 0;
	}

	/**
	 * @return int size
	 */
	public int size() {
		return size;
	}

	/**
	 * @return boolean
	 * checks if the list is empty
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	 /**
	  * Returns (but does not remove) the first element of the list
	  * @return element at the front of the list (or null if empty)
	 */
	public E first() {
		if (isEmpty()) {
			return null;
		}
		return head.getElement();
	}
	 /**
	   * Returns (but does not remove) the last element of the list.
	   * @return element at the end of the list (or null if empty)
	   */
	public E last() {
		if (isEmpty()) {
			return null;
		}
		return tail.getElement();
	}
	  // update methods
	  /**
	   * Adds an element to the front of the list.
	   * @param e  the new element to add
	   */
	  public void addFirst(E e) {               
	    head = new Node<>(e, head);             
	    if (size == 0)
	      tail = head;                           
	    size++;
	  }
	 
	  /**
	   * Adds an element to the end of the list.
	   * @param e  the new element to add
	   */
	  public void addLast(E e) {                 
	    Node<E> newest = new Node<>(e, null);    
	    if (isEmpty())
	      head = newest;                         
	    else
	      tail.setNext(newest);                  
	    tail = newest;                          
	    size++;
	  }
	/**
	 * Removes a node given an index
	 * @param index index of the node to remove
	 */
	public void removeNode(int index) {
		// Case 1. Index out of bounds
		if (index < 0 || index > this.size) {
			throw new IndexOutOfBoundsException("Invalid index: " + index);
		}
		// Case 2. If the list is empty
		if (isEmpty()) {
			return;
		}
		Node<E> curr = head;
		if (index == 0) { // Case 3. If we're removing the first node in the list
			head = head.getNext();
			// Case 3.1 If after removing the node, the list size is 0, there is no tail
			if (size == 0) {
				tail = null;
			}
			size--;
			return;
		}
		// Case 4. Search for the node to remove given the index
		for (int i = 0; curr != null && i < index - 1; i++) {
			curr = curr.next;
		}
		// Remove the node
		curr.next = curr.next.next;
		size--;
	}
	  /**
	   * Removes and returns the first element of the list.
	   * @return the removed element (or null if empty)
	   */
	public E removeFirst() {
		if (isEmpty()) {
			return null;
		}
		E answer = head.getElement();
		head = head.getNext();
		size--;
		if (size == 0) {
			tail = null;
		}
		return answer;
	}
	public E removeLast(){
		if(isEmpty()){
			return null;
		}
		Node<E> node = head;
		E answer = null;
		if(head.next==null){
			answer = head.getElement();
			head = null;
			size--;
			return answer;
		}
		while(node.next.next!=null){
			node = node.next;
		}
		answer = node.next.getElement();
		node.next=null;
		tail = node;
		size--;
		return answer;
		
	}
	/**
	 * @return the String form of all the nodes 
	 */
	public String toString() {
		Node<E> curr = head;
		StringBuilder sb = new StringBuilder();
		while (curr != null) {
			sb.append(curr.toString());
			sb.append("\n");
			curr = curr.next;
		}
		return sb.toString();
	}

}
