import java.util.Scanner;

public class CommandLineCalc {
	public static void main(String [] args) {
		Scanner input = null;
		input = new Scanner(System.in);
		if (args.length == 0 ) {
			// Convenience method for invoking program from within IDE, if comman line arguments fed to it this is skipped.		
			System.out.print("Enter an arithmetic expression of integers or doubles with (+, -, *, /, %): ");
			String str = input.nextLine();
			str.trim();
			args = str.split(" ");
		}
		/*
		for (int i = 0; i< args.length; i++) {
			System.out.println(args[i]);
		}
		*/
		/*
		//Int match
		String str = "1";
		System.out.println(str.matches("^[0-9]{1,}$"));
		//Double match
		String dbStr =" 0.22";
		System.out.println(dbStr.matches("^[0-9]{1,}\\.[0-9]{1,}$"));
		String operator = "*";
		System.out.println(operator.matches("^[+-]$"));
		System.out.println(operator.matches("^[*]$"));
		System.out.println(operator.matches("^[/%]$"));
		*/
		// in case of missing operator on end
		String [] tempArgs = null;
		boolean hasDouble = false;
		boolean divisorModulus = false;
		String expr = "";
		int i = 0;

		while (true){
			// System.out.println("At top of while i = " + i);
			try{
					//For some reason I am getting whitespace added to number arguments when calling matches such that it doesn't
					//match a second time around.
					args[i] = (args[i]).trim();
					
					if (divisorModulus && ((args[i]).matches("^[0]$"))) {
						throw new ArithmeticException();
					}
					else if ((args[i]).matches("^[0-9]{1,}$")) {
					}
					else if ((args[i]).matches("^[0-9]{1,}\\.[0-9]{1,}$")) {
						hasDouble = true;
					}
					else {
						throw new NotANumberException();
					}
					// Reset the boolean so that first branch of above if only triggered when / or % found
					divisorModulus = false;
					expr += args[i] += ' ';
					i++;
					if (i == args.length) {
						//Come to end of operands, exit while loop
						break;
					}
					args[i] = (args[i]).trim();
					// | ((args[i]).equals("*"))
					if (((args[i]).matches("^[+-]$")) | ((args[i]).equals("*"))) {
						expr += args[i] += ' ';
						// System.out.println("Matches first");
					}
					else if ((args[i]).matches("^[/%]$")) {
						divisorModulus = true;
						// System.out.println("Matches second");
						expr += args[i] += ' ';
					}
					else {
						throw new IllegalOperationException();
					}
					//On to next operator at end of operand
					i++;
					if (i == args.length) {
						throw new NotEngoughNumbersException();
					}
			}
			catch (NotANumberException ex) {
				System.out.print(args[i] + " is not a number please enter the number: ");
				args[i] = input.next();
				i = 0;
				expr = "";
			}
			catch (IllegalOperationException ex) {
				System.out.print(args[i] + " is not an operator please renter the operator: ");
				args[i] = input.next();
				// System.out.println(args[i]);
				i = 0;
				expr = "";
			}
			catch (NotEngoughNumbersException ex) {
				System.out.print("You are missing a trailing operator after " + args[i-1] + ". Please enter a number : ");
				while (!input.hasNextDouble()) {
					//Clear wrong input
					input.next();
					System.out.print("You need to enter an integer or double to complete the expression : ");
				}
				String tmp = input.next();
				expr += tmp + ' ';
				break;
			}
			catch (ArithmeticException ex ) {
				System.out.print("Zero following " + args[i-1] + " is not permissible please choose another number: ");
				args[i] = input.next();
				i = 0;
				expr = "";
			}

		}
		// System.out.println(expr + "is a good expression and double detected = " + hasDouble);
		//First evaluate all the * % and / expression from left to write
		String eval1 = "";
		Scanner exprInput = new Scanner(expr);
		double left = 0;
		double right = 0;
		double store = 0;
		String opStore = null;
		char operand;
		//Expression always starts and ends with a number
		//take everything in as a double and just truncate it based on if hasDouble = true in printOut
		left = exprInput.nextDouble();
		while (exprInput.hasNext()) {	
			opStore = exprInput.next();
			operand = opStore.charAt(0);
			right = exprInput.nextDouble();
			switch (operand) {
				case '*': 	
							store = left * right;
							left = store;
							if(!exprInput.hasNext()) {
								eval1 += String.valueOf(left);
							}
							break;
				case '/': 
							store = left / right;
							left = store;
							if(!exprInput.hasNext()) {
								eval1 += String.valueOf(left);
							}
							break;
				case '%':
							store = left % right;
							left = store;
							// Take care of when only two operands
							if(!exprInput.hasNext()) {
								eval1 += String.valueOf(left);
							}
							break;
				case '+':
				case '-':
							eval1 += String.valueOf(left) + ' ' + operand + ' ';
							left = right;
							// Take care of trailing number in + - situations, * / and % collapse operands
							if(!exprInput.hasNext()) {
								eval1 += String.valueOf(right);
							}
							break;
							
			}

		}

		
		// System.out.println("eval1 = " + eval1);
		store = 0;
		exprInput = new Scanner(eval1);
		left = exprInput.nextDouble();
		store += left;
		while (exprInput.hasNext()) {	
			opStore = exprInput.next();
			operand = opStore.charAt(0);
			right = exprInput.nextDouble();
			switch (operand) {
				// Already processed *, / and %
				case '+':
							store += right;
							break;
				case '-':
							store -= right;
							break;
							
			}
		}

		if (hasDouble) {
			System.out.printf("The calculation = %.2f%n", store);
		}
		else {
			System.out.printf("The calculation = %d%n", (int)store);
		}
				
	}
}
