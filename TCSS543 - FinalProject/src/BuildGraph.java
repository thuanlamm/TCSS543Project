import java.io.*;
import java.util.Random;

public class BuildGraph {
	public static void main(String[] args) throws Exception {
		int vertices, dense, maxCapacity, minCapacity;
		String directory, fileName;
		System.out.println("\n\n---------------------------------------------------");
		System.out.print("Enter number of nodes: \t");
		vertices = GetInt();
		System.out.print("Enter density: \t");
		dense = GetInt();
		System.out.print("Enter minimum capacity: \t\t");
		minCapacity = GetInt();
		System.out.print("Enter maximum capacity: \t\t");
		maxCapacity = GetInt();

		directory = System.getProperty("user.dir");
		System.out.print("Enter the output file name: \t\t");
		fileName = GetString() + ".txt";
		System.out.println("---------------------------------------------------\n");

		Random random = new Random();
		try {
			String dirName = directory;//
			if (dirName.equals("")) {
				dirName = ".";
			}

			File outputfile = new File(dirName, fileName);
			int[][] Graph = new int[vertices][vertices];
			int n, m;

			for (n = 0; n < vertices; n++)
				for (m = n + 1; m < vertices; m++) {
					int randomInt = (random.nextInt((maxCapacity - minCapacity + 1)) + minCapacity);

					int k = (int) (1000.0 * Math.random() / 10.0);
					int b = (k < dense) ? 1 : 0;
					if (b == 0) {
						Graph[n][m] = Graph[m][n] = b;
					} else {
						Graph[n][m] = Graph[m][n] = randomInt;
					}
				}

			PrintWriter output = new PrintWriter(new FileWriter(outputfile));

			for (int x = 0; x < Graph.length; x++) {
				if (x == 0) {
					for (int y = 0; y < Graph[x].length; y++) {
						String value = String.valueOf(Graph[x][y]);
						if (y != 0) {
							if (value.equals("0") == false) {
								output.print("s " + String.valueOf(y) + " " + value + "\n");
							}
						}
					}
				} else {
					if (x == Graph.length - 1) {
						for (int y = 0; y < Graph[x].length; y++) {
							String value = String.valueOf(Graph[x][y]);
							if (y != 0) {
								if (value.equals("0") == false) {
									output.print(String.valueOf(y) + " t " + value + "\n");
								}
							}
						}
					} else {
						for (int y = 0; y < Graph[x].length; y++) {
							String value = String.valueOf(Graph[x][y]);
							if (y != 0) {
								if (value.equals("0") == false) {
									output.print(x + " " + String.valueOf(y) + " " + value + "\n");
								}
							}
						}
					}
				}

			}

			output.close();
		} catch (IOException e) {
			System.err.println("Error opening file" + e);
			return;
		}
		System.out.print("\nDone");
	}

	// helper functions
	public static String GetString() throws IOException {
		BufferedReader stringIn = new BufferedReader(new InputStreamReader(System.in));
		return stringIn.readLine();
	}

	public static int GetInt() throws IOException {
		String aux = GetString();
		return Integer.parseInt(aux);
	}

}