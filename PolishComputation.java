import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PolishComputation {
	
	public static int compute(String line) {
		// lineInner will eventually be set to the expression in line with the outer brackets removed
		String lineInner = null;
		int result;
		// Check if the line is empty
		if (line.trim().isEmpty()) {
			System.out.print("Empty line: Error code ");
			return 1;
		}
		// Check if the line is not enclosed by brackets
		else if (line.charAt(0) != '(' && line.charAt(line.length() - 1) != ')') {
			// If not enclosed by brackets, line has to be an integer
			try {
				result = Integer.parseInt(line);
				return result;
			} 
			// If we catch an exception in Integer.parseInt, then line is not an integer so we have an error
			catch(NumberFormatException e) {
				System.out.print("Invalid format: Error code ");
				return 2;
			}
		// Check if the line is enclosed by brackets
		} else if (line.charAt(0) == '(' && line.charAt(line.length() - 1) == ')') {
			// Remove the outer brackets
			lineInner = line.substring(1, line.length() - 1);
			// Check if lineInner is empty
			if (lineInner.trim().isEmpty()) {
				System.out.print("Nothing inside brackets: Error code ");
				return 3;
			}
			// Check if lineInner only contains an integer
			try {
				result = Integer.parseInt(lineInner);
				return result;
			}
			// If we catch an exception in Integer.parseInt, lineInner is more complex than an integer
			catch(NumberFormatException e) {
				// Now we split the expression into operator and operands and add the operands to an ArrayList
				char operator = lineInner.charAt(0);
				String operands = lineInner.substring(1);
				ArrayList<String> operandsList = new ArrayList<String>();
				for (int i = 0; i < operands.length(); i++) {
					// Check if a space is always followed by an opening bracket or an integer
					if (operands.charAt(i) == ' ') {
						if (i == operands.length() - 1 || (!Character.isDigit(operands.charAt(i + 1)) && operands.charAt(i + 1) != '(')) {
							System.out.print("Invalid format: Error code ");
							return 4;
						}
					}
					// Check if the operand is a bracketed expression
					else if (operands.charAt(i) == '(') {
						int openBrackets = 1;
						int j = i + 1;
						while (j < operands.length() && openBrackets > 0) {
							if (operands.charAt(j) == '(') {
								openBrackets++;
							} else if (operands.charAt(j) == ')') {
								openBrackets--;
							}
							j++;
						}
						operandsList.add(operands.substring(i, j));
						i = j - 1;
					// Check if the operand is an integer without brackets
					} else if (Character.isDigit(operands.charAt(i))) {
						int j = i + 1;
						while (j < operands.length() && Character.isDigit(operands.charAt(j))) {
							j++;
						}
						operandsList.add(operands.substring(i, j));
						i = j - 1;
					} else {
						System.out.print("Invalid format: Error code ");
						return 5;
					}
				}
				// If there are less than two operands, we have an error
				if (operandsList.size() < 2) {
					System.out.print("Less than two arguments: Error code ");
					return 6;
				}
				// Set result as the value of the first operand
				result = compute(operandsList.remove(0));
				// Recursively operate on the result to find the final value of the expression
				for (String operand : operandsList) {
					if (operator == '+') {
						result += compute(operand);
					} else if (operator == '-') {
						result -= compute(operand);
					} else if (operator == '*') {
						result *= compute(operand);
					} else if (operator == '\\') {
						result /= compute(operand);
					} else {
						System.out.print("Invalid operator: Error code ");
						return 7;
					}
				}
				return result;
			}
		} else {
			System.out.print("Invalid format: Error code ");
			return 8;
		}
	}
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Incorrect number of files supplied: Error code 9");
			System.exit(1);
		}
		try {
			FileReader fileReader = new FileReader(args[0]);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				// Compute line-by-line the result of all the computations in the file and print to console
				System.out.println(compute(line));
			}
			bufferedReader.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("Unable to open file: Error code 10");
		}
		catch(IOException e) {
			System.out.println("Failure while reading file: Error code 11");
		}
	}
}