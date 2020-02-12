/**
 * @author Heon Jae Jeong, Vivek Mogili, Huy Thanh Le
 *
 */
public class Queue<E> {
	private SinglyLinkedList<E> list;

	public Queue() {
		list = new SinglyLinkedList<E>();
	}

	public boolean isEmpty() {
		return list.size() == 0;
	}

	public int size() {
		return list.size();
	}

	public void enqueue(E e) {
		if (isEmpty()) {
			list.addFirst(e);
		} else {
			list.addLast(e);
		}
	}

	public E dequeue() {
		return list.removeFirst();
	}
}
