import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.lang.Math;

public class NumericalIntegration {
	public static void main(String[] args) {
		boolean vflag = false;
		boolean sflag = false; 
		boolean aflag = false;
		boolean bflag = false;
		boolean nflag = false;
		boolean fnflag = false;
		boolean tflag = false;

		double a = 0.;
		double b = 0.;
		int N = 0;
		String date = date();
		String name = "";

		for (int i=0; i<args.length; i++) {
			String arg = args[i];
			if (arg.equals("-v")) {
				vflag = true;
				System.out.println("Verbose mode on.");
			} 
			else if (arg.equals("-h")) {
				System.out.println();
				System.out.println("Numerical Integration Help Dialogue");
				System.out.println("-------------------------------------------------------------------------------------------------");
				System.out.println();
				System.out.println("Usage: java NumericalIntegration [-v] [-args]");
				System.out.println("Example: java NumericalIntegration -v -fn results -a -2 -b 5 -n 10");
				System.out.println();
				System.out.println();
				System.out.println("Args:");
				System.out.println("-v: Toggles verbose mode, must be first for correct operation.");
				System.out.println("-h: Produces this dialogue.");
				System.out.println("-s: Uses Scanner to prompt user for variables");
				System.out.println("-fn: Sets filename, defaults to output.dat. Will not append file extension.");
				System.out.println("-t: Prints results to terminal instead of file.");
				System.out.println("-a: Specifies lower limit of integration.");
				System.out.println("-b: Specifies upper limit of integration.");
				System.out.println("-n: Specifies number of iterations.");
				System.exit(0);
			}
			else if (arg.equals("-s")) {
				sflag = true;

				if (vflag == true) 
					System.out.println("Using Scanner for input");
			}
			else if (arg.equals("-fn")) {
				fnflag = true;
				name = args[i+1] + ".txt";

				if (vflag == true)
					System.out.println("Printing results to " + name);
			}
			else if (arg.equals("-t")) {
				tflag = true;

				if (vflag == true) 
					System.out.println("Printing result to terminal");
			}
			else if (arg.equals("-a")) {
				aflag = true;

				try {
					a = Double.valueOf(args[i+1]);
				} catch (NumberFormatException E) {
					System.err.println("Lower limit is not a number! Error details: " + E.getMessage() + "\n Exiting Program.");
					System.exit(0);
				} finally {

				}

				if (vflag == true) 
					System.out.println("Lower limit is " + a);
			}
			else if (arg.equals("-b")) {
				bflag = true;
				try {
					b = Double.valueOf(args[i+1]);
				} catch (NumberFormatException E) {
					System.err.println("Upper limit is not a number! Error details: " + E.getMessage() + "\n Exiting Program.");
					System.exit(0);
				} finally {
					
				}

				if (vflag == true) 
					System.out.println("Upper limit is " + b);
			}
			else if (arg.equals("-n")) {
				nflag = true;
				try {
					N = Integer.parseInt(args[i+1]);
				} catch (NumberFormatException E) {
					System.err.println("Number of iterations is not an integer!  Error details: " + E.getMessage() + "\n Exiting Program.");
					System.exit(0);
				} finally {
					
				}

				if (vflag == true) 
					System.out.println("Number of iterations is " + args[i+1]);
			}
		}

		if (sflag == true) {
			Scanner sc = new Scanner(System.in);

			System.out.println("Enter lower integration limit:");
			a = sc.nextDouble();
		
			System.out.println("Enter upper integration limit:");
			b = sc.nextDouble();

			System.out.println("Enter number of sampling points:");
			N = sc.nextInt();
		} else if (aflag == false) {
			System.out.println("Error: No lower integration limit given! Exiting Program.");
			System.exit(0);
		} else if (bflag == false) {
			System.out.println("Error: No upper integration limit given! Exiting Program.");
			System.exit(0);
		} else if (nflag == false) {
			System.out.println("Error: No number of iterations given! Exiting Program.");
			System.exit(0);
		}

		long leftTime1 = System.currentTimeMillis();
		double leftResult = leftEndpoint(a, b, N);
		long leftTime2 = System.currentTimeMillis() - leftTime1;
		if (vflag == true) 
			System.out.println("Area from left-endpoint rule calculated as " + leftResult + " in " + leftTime2 + "ms.");

		long midTime1 = System.currentTimeMillis();
		double midResult = midEndpoint(a, b, N);
		long midTime2 = System.currentTimeMillis() - midTime1;
		if (vflag == true) 
			System.out.println("Area from mid-endpoint rule calculated as " + midResult + " in " + midTime2 + "ms.");

		long trapTime1 = System.currentTimeMillis();
		double trapeziumResult = trapezium(a, b, N);
		long trapTime2 = System.currentTimeMillis() - trapTime1;
		if (vflag == true) 
			System.out.println("Area from trapezium rule calculated as " + trapeziumResult + " in " + trapTime2 + "ms.");

		if (tflag == true) {
			System.out.println("The left-endpoint rule result is: " + leftResult + ", calculated in " + leftTime2 + "ms.");
			System.out.println("The mid-endpoint rule result is: " + midResult  + ", calculated in " + midTime2 + "ms.");
			System.out.println("The trapezium rule result is: " + trapeziumResult  + ", calculated in " + trapTime2 + "ms.");
		}
		else {
			if (fnflag == false) 
				name = "output";

			name += ".dat";

			try {
				FileWriter writer = new FileWriter(name, true);
				writer.write(a + "\t" + b + "\t" + N + "\t" + leftResult + "\t" + leftTime2 + "\t" + midResult + "\t" + midTime2 + "\t" + trapeziumResult + "\t" + trapTime2 + System.lineSeparator());
				writer.flush();
				writer.close();

				if (vflag == true) 
					System.out.println("Printing " +  a + "\t" + b + "\t" + N + "\t" + leftResult + "\t" + leftTime2 + "\t" + midResult + "\t" + midTime2 + "\t" + trapeziumResult + "\t" + trapTime2 + " to file " + name);
			} catch (IOException E) {
				System.err.println("IOException: " + E.getMessage());
				System.exit(0);
			} finally {
				if (vflag == true) 
					System.out.println("Printed Line");
			}

		}
	}

	public static String date() {
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd_HH-mm-ss");
    	Date dateobj = new Date();
    	String date = df.format(dateobj);
    	return date;
	}

	public static double f(double x) {
		return (5*Math.sin(Math.log(x*x))*x*x*x);
	}

	public static double leftEndpoint(double a, double b, int N) {
		double result = 0.0;
		double h = (b-a)/N;
	
		for (int i=1; i<=N; i++) {
			double f_x = f(a + h*(i-1));
			result += h*f_x;
		}

		return result;
	}

	public static double midEndpoint(double a, double b, int N) {
		double result = 0.0;
		double width = (b-a)/N;

		for (int i=1; i<=N; i++) {
			double x_i = a + width*(i-0.5); 
			result += width*f(a + width*(i-0.5));
		}

		return result;
	}

	/*
	public static double trapezium(double a, double b, int N) {
		double width = (b-a)/(N-1);
		double result = 0.5*width*(f(a) + f(b));

		for (int i=2; i<=(N-1); i++) {
			double x_i = a + width*(i-1); 
			result += width*f(x_i);
		}

		return result;
	}
	 */

	public static double  trapezium(double a, double b, int N) {
		double width = (b-a)/N;
		double result = 0.0;

		for (int i=1; i<=N; i++) {
			double x_i = a + width*(i-1);
			double x_i1 = a + width*i;

			double f_x_i = f(x_i);
			double f_x_i1 = f(x_i1);

			result += width*(f_x_i + f_x_i1)*0.5;
		}

		return result;
	}
}