/**
 * @author Heon Jae Jeong, Vivek Mogili, Huy Thanh Le
 *
 */
public class Stack<E> {
	private SinglyLinkedList<E> list;

	public Stack() {
		list = new SinglyLinkedList<E>();
	}

	public boolean isEmpty() {
		return list.size() == 0;
	}

	public int size() {
		return list.size();
	}

	public void push(E e) {
		if (isEmpty()) {
			list.addFirst(e);
		} else {
			list.addLast(e);
		}
	}

	public E pop() {
		return list.removeLast();
	}
}
