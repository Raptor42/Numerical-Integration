import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.io.FileWriter;
import java.io.IOException;

public class stats {
	public static boolean vflag = false;

	public static void main(String[] args) {
		boolean fnflag = false;
		boolean tflag = false;
		boolean pflag = false;
		boolean cflag = false;

		int column = 0;

		String filename = new String("");
		Scanner sc = new Scanner(System.in);

		for (int i=0; i<args.length; i++) {
			String arg = args[i];

			if (arg.equals("-fn")) {
				fnflag = true;
				filename = args[i+1];
			} 
			else if (arg.equals("-h")) {
				System.out.println("Stats Program Help Dialogue");
				System.out.println("-----------------------------------------------------------------------------------------------");
				System.out.println();
				System.out.println("Usage: java stats [-v] [-args]");
				System.out.println("Example: java NumericalIntegration -v -fn results.dat -c 3");
				System.out.println();
				System.out.println();
				System.out.println("Args:");
				System.out.println("-h: Produces this dialogue");
				System.out.println("-fn: Specifies file to read data from. File extension must be included. Defaults to output.txt.");
				System.out.println("-v: Enables Verbose mode. Must be enabled first for proper operation.");
				System.out.println("-p: Prints data from the file");
				System.out.println("-c: Specifies working column");
				System.exit(0);
			} 
			else if (arg.equals("-v")) {
				vflag = true;
				System.out.println("Verbose Mode on");
			}
			else if (arg.equals("-t")) {
				tflag = true;
				if (vflag == true) 
					System.out.println("Printing to terminal");
			}
			else if (arg.equals("-p")) {
				pflag = true;
				if (vflag == true) 
					System.out.println("Printing data");
			}
			else if (arg.equals("-c")) {
				cflag = true;
				column = Integer.parseInt(args[i+1]);

				if (vflag == true) 
					System.out.println("Using " + column + " as the working column");
			}
		}

		if (fnflag == false)
			filename = "output.txt";

		if (vflag == true) 
			System.out.println("Reading from " + filename);

		double[][] contents = returnContents(filename);
		// printMultiArray(contents);

		if (cflag == false) {
			System.out.println("Which column of data do you wish to perform stats on?");
			column = sc.nextInt() - 1;
		}

		while (column > contents[1].length) {
			System.out.println("Error: column does not exist. Please enter a valid integer, or enter 0 to exit.");
			column = sc.nextInt() - 1;
			if (column == -1)
				System.exit(0);
		}

		int fileLength = contents.length;
		double[] data = new double[fileLength];

		for (int i = 0; i<fileLength; i++) 
			data[i] = contents[i][column];

		double mean = getMean(data);
		double stdDev = getStdDev(data);
		double stdErr = getStdErr(data);

		if (tflag == true) {
			System.out.println("Mean: " + mean);
			System.out.println("Standard Deviation: " + stdDev);
			System.out.println("Standard Error: " + stdErr);
			printMultiArray(contents);
		} else {
			try {
				String name = "results_" + filename;
				FileWriter writer = new FileWriter(name, true);
				writer.write("Mean: " + mean + "\t");
				writer.write("Standard Deviation: " + stdDev + "\t");
				writer.write("Standard Error: " + stdErr + "\t");
				writer.write(System.lineSeparator());
				writer.flush();
				writer.close();
			} catch(IOException e) {
				System.err.println("IOException: " + e.getMessage());
				System.exit(0);
			}
		}
	}

	public static double[][] returnContents(String filename) {
		double[][] contents;
		int fileLength = 0;
		int lineLength = 0;
		int maxLineLength = 0;
		String lineContents = "";
		
		File file = new File(filename);

		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				lineContents = sc.nextLine();
				if (vflag == true) 
					System.out.println("New Line");

				Scanner sc2 = new Scanner(lineContents);

				if (vflag == true) 
					System.out.println("Line Contents: " + lineContents);

				if (vflag == true) 
					System.out.println("Finding line length");
				while(sc2.hasNextDouble()) {
					lineLength ++;
					double foo = sc2.nextDouble();
					if (vflag == true) 
						System.out.println("Current line length: " + lineLength);
				}

				if (lineLength>maxLineLength) {
					maxLineLength = lineLength; 
					if (vflag == true) 
						System.out.println("New max line length: " + maxLineLength);
				}
				
				fileLength ++;
				if (vflag == true) 
					System.out.println("Current file length: " + fileLength);

				sc2.reset();
				sc2.close();
				lineLength = 0; 
			}

			sc.reset();
			sc.close();
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
			System.exit(0);
		}

		contents = new double[fileLength][maxLineLength];

		try {
			Scanner sc = new Scanner(file);
			for (int i=0; i<fileLength; i++) {
				for (int j=0; j<maxLineLength; j++)
					contents[i][j] = sc.nextDouble();
			}
		} catch(FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
			System.exit(0);
		}

		return contents;	
	}

	public static void printArray(double[] array) {
		int length = array.length;
		for (int i=0; i<length; i++) 
			System.out.print(array[i] + ", ");

		System.out.print("\n");
	}

	public static void printMultiArray(double[][] array) {
		int length = array.length;
		for (int i=0; i<length; i++)
			printArray(array[i]);
	}

	public static double getMean(double[] set) {
		int length = set.length;
		double mean = 0.0;

		for (int i=0; i<length; i++) 
			mean += set[i] / length;
		
		return mean;
	}

	public static double getStdDev(double[] set) {
		int length = set.length;
		double mean = getMean(set);
		double sum = 0.0;


		for (int i=0; i<length; i++) {
			double diff = set[i] - mean;
			sum += diff * diff;
		}

		double stdDev = sum/(length-1);

		stdDev = Math.pow(sum, 0.5);

		return stdDev;
	}

	public static double getStdErr(double[] set) {
		int length = set.length;
		double stdDev = getStdDev(set);
		double stdErr = stdDev/(Math.pow(length, 0.5));
		return stdErr;
	}

}