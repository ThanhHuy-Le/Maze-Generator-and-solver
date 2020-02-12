import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Heon Jae Jeong, Vivek Mogili, Huy Thanh Le
 *
 */
public class MazeDrawer extends JPanel {
	private static final int x = 50;
	private static final int y = 50;
	private List<Integer> path;
	private int dim;
	private Room[] rooms;
	private int panelX, panelY;

	public MazeDrawer(Room[] rooms, ArrayList<Integer> path) {
		this.rooms = rooms;
		this.path = path;
		this.dim = rooms.length;
		panelX = (x*rooms.length/2)+(x*rooms.length/4);
		panelY = (y*rooms.length/4)+(y*rooms.length/8);
		this.show();
	}

	@Override
	public void paintComponent(Graphics g) {
		// This portion draws the empty Maze
		int xCord = 50;
		int tempXCord = xCord;
		int yCord = 100;
		for (int i = 0; i < rooms.length; i++) {
			if (i % Math.sqrt(dim) == 0) {
				yCord = yCord + y;
				xCord = tempXCord;
			}
			drawSquare(xCord, yCord, i, g);
			xCord = xCord + x;
		}
		yCord = 100;
		// End drawing of empty maze
		drawPath(xCord + (dim + (panelX / 6)), yCord, g);
	}

	public void drawPath(int xCord, int yCord, Graphics g) {
		// This portion draws the solution Maze
		int tempxCord = xCord;
		int[] newMaze = new int[dim];
		for (int i = 0; i < newMaze.length; i++) {
			newMaze[i] = 1;
		}
		for (int i = 0; i < path.size(); i++) {
			newMaze[path.get(i)] = 0;
		}
		for (int i = 0; i < dim; i++) {
			if (i % Math.sqrt(dim) == 0) {
				yCord = yCord + y;
				xCord = tempxCord;
			}
			if (newMaze[i] == 0) {
				g.fillRect(xCord, yCord, x, y);
				g.setColor(Color.WHITE);
				g.drawString(Integer.toString(i), (xCord+xCord+x)/2, (yCord+yCord+y)/2);
				g.setColor(Color.BLACK);
			} else {
				drawSquare(xCord, yCord, i, g);
			}
			xCord = xCord + x;
		}
	}

	public void drawSquare(int xCord, int yCord, int index, Graphics g) {
		int[] doors = rooms[index].getDoors();
		for (int j = 0; j < doors.length; j++) {
			if (j == 0 && doors[0] == 1) {
				g.drawLine(xCord, yCord, xCord + x, yCord);
			} else if (j == 1 && doors[2] == 1) {
				g.drawLine(xCord + x, yCord, xCord + x, yCord + y);
			} else if (j == 2 && doors[1] == 1) {
				g.drawLine(xCord + x, yCord + y, xCord, yCord + y);
			} else if (j == 3 && doors[3] == 1) {
				g.drawLine(xCord, yCord + y, xCord, yCord);
			}
		}
		g.drawString(Integer.toString(index), (xCord+xCord+x)/2, (yCord+yCord+y)/2);
		
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(panelX, panelY);
	}

	public void show() {
		JFrame frame = new JFrame("Maze - Term Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel label = new JLabel("Maze (Left) ------------ Shortest Path(Right)");
		label.setFont(new Font("Arial", Font.BOLD, 32));
		this.add(label);
		frame.getContentPane().add(this);
		frame.pack();
		frame.setVisible(true);
	}
}
