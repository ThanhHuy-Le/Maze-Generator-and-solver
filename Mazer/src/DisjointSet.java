/**
 * @author Heon Jae Jeong, Vivek Mogili, Huy Thanh Le
 *
 */
public class DisjointSet {
	private int[] set;

	public DisjointSet(int dim) {
		set = new int[dim * dim];
		for (int i = 0; i < set.length; i++) {
			set[i] = -1;
		}
	}

	public int find(int index) {
		if (set[index] < 0) {
			return index;
		} else {
			return find(set[index]);
		}
	}

	public void union(int index1, int index2) {
		// Only happens when both index1 and index2 have same root
		if (this.find(index1) != this.find(index2)) {
			// If they both have different roots, find them
			int root1 = find(index1);
			int root2 = find(index2);

			if (set[root1] <= set[root2]) {
				set[root1] += set[root2];
				set[root2] = root1;
			} else {
				set[root2] += set[root1];
				set[root1] = root2;
			}
		}
	}

	public void print() {
		for (int i = 0; i < set.length; i++) {
			System.out.print(set[i]);
			System.out.print(" ");
		}
		System.out.println();
	}
}