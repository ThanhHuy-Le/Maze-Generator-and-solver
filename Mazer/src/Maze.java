import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Heon Jae Jeong, Vivek Mogili, Huy Thanh Le
 *
 */
public class Maze {
	private int dim;
	private Room[] maze;
	private ArrayList<Integer> shortestPath;
	public Maze() {
		dim = 0;
		maze = null;
	}

	/**
	 * Prompts user for choice, 1==random maze and 2==read maze from file
	 */
	public void start() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter 1 to generate a random maze\nEnter 2 to read maze from file");
		int response = Integer.parseInt(sc.nextLine());
		if (response == 1) {
			System.out.println("Enter dimensions of maze");
			this.dim = Integer.parseInt(sc.nextLine());
			generateMaze();
		} else if (response == 2) {
			readMazeFromFile();
		}
		printMaze();
		BFS();
		DFS();
		MazeDrawer mzd = new MazeDrawer(maze,shortestPath);
		sc.close();
	}

	/**
	 * Generates a random maze with dimension n given in start()
	 */
	public void generateMaze() {
		maze = new Room[dim * dim];
		int[] door0 = { 0, 1, 1, 1 };
		maze[0] = new Room(0, door0, generateAdjacents(0));

		int[] door = { 1, 1, 1, 1 };
		for (int i = 1; i <= dim * dim - 2; i++) {
			maze[i] = new Room(i, door, generateAdjacents(i));
		}

		int[] doorN = { 1, 0, 1, 1 };
		maze[dim * dim - 1] = new Room(dim * dim - 1, doorN, generateAdjacents(dim * dim - 1));
		// while loop with disjoint set to open doors
		DisjointSet rooms = new DisjointSet(dim);
		while (rooms.find(0) != rooms.find(dim * dim - 1)) {
			Random ran = new Random();
			int r1 = -1;
			int r2 = -1;

			r1 = ran.nextInt(dim * dim);
			r2 = findAdjacent(r1);

			// r1 and r2 are adjacent
			if (rooms.find(r1) != rooms.find(r2)) {
				int r1AdjDoor = findAdjDoor(r1, r2)[0];
				int r2AdjDoor = findAdjDoor(r1, r2)[1];
				int[] doorNr1 = { maze[r1].getDoor(0), maze[r1].getDoor(1), maze[r1].getDoor(2), maze[r1].getDoor(3) };
				doorNr1[r1AdjDoor] = 0;
				maze[r1].setDoor(doorNr1);
				int[] doorNr2 = { maze[r2].getDoor(0), maze[r2].getDoor(1), maze[r2].getDoor(2), maze[r2].getDoor(3) };
				doorNr2[r2AdjDoor] = 0;
				maze[r2].setDoor(doorNr2);
				rooms.union(rooms.find(r1), rooms.find(r2));
			}
		}
		try {
			outputToText();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads maze from file called "maze.txt"
	 */
	public void readMazeFromFile() {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("maze.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		dim = Integer.parseInt(sc.nextLine());
		maze = new Room[dim * dim];

		for (int index = 0; sc.hasNextLine(); ++index) {
			String nextl = sc.nextLine();
			String[] next = nextl.split(" ");
			if (next.length != 4) {
				break;
			}

			int[] doors = new int[next.length];

			for (int i = 0; i < doors.length; ++i) {
				doors[i] = Integer.parseInt(next[i]);
			}
			maze[index] = new Room(index, doors, generateAdjacents(index));
		}
		sc.close();
	}

	/********* BFS and DFS go here *************/
	public void BFS() {
		Queue<Integer> queue = new Queue<Integer>();
		// will contain bfs path
		ArrayList<Integer> bfsVisits = new ArrayList<Integer>();
		bfsVisits.add(0);
		// Enqueue room number 0 to Q
		queue.enqueue(0);
		// Mark room 0 as visited
		maze[0].setVisited(true);

		while (!queue.isEmpty()) {
			int num = (int) queue.dequeue();
			if (num == maze.length - 1)
				break;
			int[] adjacents = generateAdjacents(num);
			int chosen = -1;
			for (int i = 0; i < adjacents.length; i++) {
				if (adjacents[i] != -1 && maze[num].getDoor(i) == 0 && !maze[adjacents[i]].isVisited()) {
					bfsVisits.add(adjacents[i]);
					queue.enqueue(adjacents[i]);
					maze[adjacents[i]].setVisited(true);
					chosen = adjacents[i];
				}
			}
			if (chosen == maze.length - 1)
				break;
		}
		// print path here
		System.out.println("Rooms visited by BFS : " + bfsVisits.toString());
		getShortestPath(bfsVisits);
		resetMaze();
	}

	private void DFS() {
		Stack<Integer> stack = new Stack<Integer>();
		// will contain dfs path
		ArrayList<Integer> dfsVisits = new ArrayList<Integer>();
		dfsVisits.add(0);
		// Push room number 0 to stack
		stack.push(0);
		// Mark room 0 as visited
		maze[0].setVisited(true);

		while (!stack.isEmpty()) {
			int num = (int) stack.pop();
			if (num == maze.length - 1)
				break;
			int[] adjacents = generateAdjacents(num);
			for (int i = 0; i < adjacents.length; i++) {
				if (adjacents[i] != -1 && maze[num].getDoor(i) == 0 && !maze[adjacents[i]].isVisited()) {
					stack.push(maze[adjacents[i]].getNumber());
					dfsVisits.add(maze[adjacents[i]].getNumber());
					maze[adjacents[i]].setVisited(true);
				}
			}
		}
		// print path here
		System.out.println("Rooms visited by DFS : " + dfsVisits.toString());
		getShortestPath(dfsVisits);
	}

	private void resetMaze() {
		for (int i = 0; i < this.maze.length; i++) {
			maze[i].setVisited(false);
		}
	}

	private void getShortestPath(ArrayList<Integer> visits) {
		shortestPath = new ArrayList<Integer>();
		// last element of maze

		// set last element as selector and put it into shortestPath
		int selector = visits.get(visits.size() - 1);
		shortestPath.add(selector);

		int i = 1;
		while (selector != 0 || i != visits.size() - 2) {
			int potSelector = visits.get(visits.size() - i);
			if (findAdjDoor(selector, potSelector)[0] != -1
					&& maze[selector].getDoors()[findAdjDoor(selector, potSelector)[0]] == 0) {
				selector = potSelector;
				shortestPath.add(selector);
				if (selector == 0)
					break;
			}
			i++;
		}
		System.out.print("This is the path (in reverse) : " + shortestPath.toString());
		drawPath(shortestPath);
	}

	/**
	 * Prints the maze in text
	 */
	public void printMaze() {
		// First row of the maze
		for (int row = 0; row < dim; row++) {
			System.out.print("|");
			for (int i = 0; i < dim; i++) {
				if (maze[i + (row * dim)].getDoor(0) == 1)
					System.out.print("¯");
				else {
					System.out.print(" ");
				}
				if (maze[i + (row * dim)].getDoor(2) == 1) {
					System.out.print("|");
				} else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}

		for (int i = dim * dim - dim; i < dim * dim; i++) {
			if (maze[i].getDoor(1) == 1)
				System.out.print(" ¯");
		}
		System.out.println();
	}

	/**************** Private methods *******************/
	private int findAdjacent(int r1) {
		int[] r1Adjacents = generateAdjacents(r1);
		int selection = -1;
		while (selection < 0) {
			Random ran = new Random();
			selection = r1Adjacents[ran.nextInt(4)];
		}
		return selection;
	}

	public int[] findAdjDoor(int r1, int r2) {
		int[][] vals = { { 0, 1 }, { 1, 0 }, { 2, 3 }, { 3, 2 }, { -1 } };
		int comp = r2 - r1;
		if (comp == -dim)
			return vals[0];
		else if (comp == dim)
			return vals[1];
		else if (comp == 1)
			return vals[2];
		else if (comp == -1)
			return vals[3];
		else
			return vals[4];
	}

	private int[] generateAdjacents(int index) {
		int[] adjacents = new int[4];
		adjacents[0] = (index - dim >= 0) ? index - dim : -1;
		adjacents[1] = (index + dim <= dim * dim - 1) ? index + dim : -1;
		adjacents[2] = (index % dim != dim - 1) ? index + 1 : -1;
		adjacents[3] = (index % dim != 0) ? index - 1 : -1;

		return adjacents;
	}

	/**
	 * If a random maze is generated, saves the maze to a txt file
	 * 
	 * @throws IOException
	 */
	private void outputToText() throws IOException {
		FileWriter fw = new FileWriter("randomMaze.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(String.valueOf(this.dim));
		bw.newLine();
		for (int i = 0; i < this.maze.length; i++) {
			int[] currDoor = maze[i].getDoors();
			for (int j = 0; j < currDoor.length; j++) {
				bw.write(String.valueOf(currDoor[j]));
				bw.write(" ");
			}
			bw.newLine();
		}
		bw.close();
		fw.close();
		System.out.println("'randomMaze.txt' created");
	}

	/**
	 * Draws the path using a series of X characters
	 */
	private void drawPath(ArrayList<Integer> bfsVisits) {
		String[] xMarks = new String[dim * dim];
		// Intially blank
		String padded = String.format("%-2s", " ");
		for (int i = 0; i < xMarks.length; i++) {
			xMarks[i] = padded;
		}
		for (int i = 0; i < bfsVisits.size(); i++) {
			xMarks[bfsVisits.get(i)] = " X";
		}
		for (int i = 0; i < xMarks.length; i++) {
			if (i % dim == 0) {
				System.out.println();
			}
			System.out.print(xMarks[i]);
		}
		System.out.println("\n");
	}

}
