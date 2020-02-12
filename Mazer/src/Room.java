/**
 * @author Heon Jae Jeong, Vivek Mogili, Huy Thanh Le
 *
 */
public class Room {
	private int number;
	private int[] doors;
	private int[] adjacents;
	private boolean visited;

	public Room(int newNum, int[] newDoors, int[] adjacents) {
		this.number = newNum;
		this.doors = newDoors;
		this.adjacents = adjacents;
		this.visited = false;
	}

	public void print() {
		for (int i = 0; i < doors.length; i++) {
			System.out.print(doors[i]);
			System.out.print(" ");
		}
		System.out.println();
	}

	public int[] getDoors() {
		return this.doors;
	}

	public int getDoor(int index) {
		return doors[index];
	}

	public void setDoor(int[] doors) {
		this.doors = doors;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public boolean isVisited() {
		return this.visited;
	}

	public int getNumber() {
		return this.number;
	}

	public int[] getAdjacents() {
		return this.adjacents;
	}

}