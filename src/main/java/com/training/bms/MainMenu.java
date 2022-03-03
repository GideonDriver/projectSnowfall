package com.training.bms;

import java.util.Scanner;

import com.training.bms.dao.CustomerDAO;
import com.training.bms.dao.CustomerDAOImpl;
import com.training.bms.dao.EventLogDAO;
import com.training.bms.dao.EventLogDAOImpl;
import com.training.bms.dao.LoginDAO;
import com.training.bms.dao.LoginDAOImpl;
import com.training.bms.exceptions.InvalidPasswordSizeException;
import com.training.bms.exceptions.InvalidUsernameSizeException;
import com.training.bms.model.Login;

public class MainMenu {

	Scanner scanner;
	int choice;
	LoginDAO loginDAO = new LoginDAOImpl();
	CustomerDAO customerDAO = new CustomerDAOImpl();
	EventLogDAO eventLogDAO = new EventLogDAOImpl();
	Login login = new Login();

	public void startApp() {

		// declare local variables for input
		int choice = 0;
		String userNameConfirm = null;
		String userPasswordConfirm = null;
		int noOfAttempts = 0;

		System.out.println("   _____ ___ _ _____ _ _ _ _____ _____ _    _       ");
		System.out.println("  |  ___|   | |  _  | | | |  ___|  _  | |  | |      ");
		System.out.println("  |___  | | | | |_| | | | |  _| |  _  | |__| |__    ");
		System.out.println("  |_____|_|___|_____|_____|_|   |_| |_|____|____|   ");
		System.out.println();

		while (true) {

			System.out.println();
			System.out.println("---MAIN MENU---");
			System.out.println("| 1. Login    |");
			System.out.println("| 2. Sign Up  |");
			System.out.println("| 9. EXIT     |");
			System.out.println("---------------");

			try {
				scanner = new Scanner(System.in);
				choice = scanner.nextInt();
				//choice = 1; //###QuickUserLogin
			} catch (Exception e) {
				choice = 0;
			}
			// ============LOGIN============
			switch (choice) {
			case 1:
				System.out.println("\n---Login---");

				noOfAttempts = 3;
				while (noOfAttempts > 0) {
					System.out.println("Username:");
					login.setUsername(scanner.next());
					//login.setUsername("Driver"); //###QuickUserLogin

					System.out.println("Password:");
					login.setPassword(scanner.next());
					//login.setPassword("password"); //###QuickUserLogin

					noOfAttempts--;

					if (!loginDAO.validate(login)) {
						login.setUserId(0);
						login.setUserType("Invalid");
						login.setUserStatus("Invalid");
					}

					if (login.getUserStatus().equals("Active")) {
						System.out.println("\nWelcome back " + login.getUsername() + "!");
						noOfAttempts = 0;
						if (login.getUserType().equals("Customer")) {
							// ###Invoke method for customer menu
							CustomerMenu customerMenuObject = new CustomerMenu();
							customerMenuObject.customerMenu(login, null);
						} else if (login.getUserType().equals("Employee")) {
							// ###Invoke method for employee menu
							EmployeeMenu employeeMenuObject = new EmployeeMenu();
							employeeMenuObject.employeeMenu(login);
						}
					} else if (login.getUserStatus().equals("Pending") || login.getUserStatus().equals("Denied")
							|| login.getUserStatus().equals("Deactivated")) {
						System.err.println("\nAccount status: " + (login.getUserStatus()));
						noOfAttempts = 0;
					} else if (noOfAttempts <= 0) {
						System.out.println(
								"\nExceeded login attempts, please contact support at HelpDesk@Snowfall.net to reset your password");
					} else {
						System.out.println("\nInvalid username/password\n(1. Try again | 9. Main Menu):");
						try {
							scanner = new Scanner(System.in);
							choice = scanner.nextInt();
							System.out.println();
						} catch (Exception e) {
							choice = 0;
						}

						switch (choice) {
						case 1:

							break;
						case 9:
							System.out.println("\nReturning to the main menu");
							noOfAttempts = 0;
							break;

						default:
							System.out.println("\n###Invalid choice, returning to the main menu");
							noOfAttempts = 0;
							break;
						}
					}
				}

				break;

			// ============SIGNUP============
			case 2:
				System.out.println("\n---Sign Up---");
				Login newUser = new Login();

				do {
					try {
						System.out.println("Username:");
						newUser.setUsername(scanner.next());

						if (loginDAO.usernameTaken(newUser.getUsername())) {
							throw new com.training.bms.exceptions.UserAlreadyExistsException(
									"Username taken, please enter a different username");
						} else if (newUser.getUsername().length() < 4 || newUser.getUsername().length() > 20) {
							throw new InvalidUsernameSizeException(
									"Username must be between 4-20 characters long, please try again");
						}

						System.out.println("Confirm Username:");
						userNameConfirm = scanner.next();
						if (!newUser.getUsername().equals(userNameConfirm)) {
							System.err.println("\nUsernames do not match, please try again");
							userNameConfirm = null;
						}
					} catch (com.training.bms.exceptions.UserAlreadyExistsException e) {
						System.out.println();
						System.err.println(e.getMessage());
					} catch (com.training.bms.exceptions.InvalidUsernameSizeException e) {
						System.out.println();
						System.err.println(e.getMessage());
					}
				} while (!newUser.getUsername().equals(userNameConfirm));

				do {
					try {
						System.out.println("Password:");
						newUser.setPassword(scanner.next());
						if (newUser.getPassword().length() < 8 || newUser.getPassword().length() > 20) {
							throw new InvalidPasswordSizeException(
									"Password must be between 8-20 characters long, please try again");
						}

						System.out.println("Confirm Password:");
						userPasswordConfirm = scanner.next();
						if (!newUser.getPassword().equals(userPasswordConfirm)) {
							System.err.println("Passwords do not match, please try again\n");
							userPasswordConfirm = null;
						}
					} catch (InvalidPasswordSizeException e) {
						System.out.println();
						System.err.println(e.getMessage());
					}
				} while (!newUser.getPassword().equals(userPasswordConfirm));

				do {
					System.out.println("\nAre you an employee at Snowfall?\n(1. Yes | 2. No):");
					try {
						scanner = new Scanner(System.in);
						choice = scanner.nextInt();
					} catch (Exception e) {
						choice = 0;
					}
					if (choice == 1) {

						System.out.println("Enter employee ID:");
						try {
							scanner = new Scanner(System.in);
							newUser.setUserId(scanner.nextInt());
						} catch (Exception e) {
							newUser.setUserId(0);
						}
						// Employee ID's are 8 digits long and start with 9
						if (newUser.getUserId() > 89999999 && newUser.getUserId() < 100000000
								&& !loginDAO.employeeIdTaken(newUser.getUserId())) {
							newUser.setUserType("Employee");
						} else {
							if (loginDAO.employeeIdTaken(newUser.getUserId())) {
								System.err.println(
										"\nID already in use, please contact support at HelpDesk@Snowfall.net");
							} else {
								System.err.println("\nInvalid ID, exiting to main menu.");
							}
							newUser.setUserId(0);
							break;
						}

					} else if (choice == 2) {
						newUser.setUserId(1);
						newUser.setUserType("Customer");
					} else {
						System.err.println("\nInvailid choice. Please enter (1-2)");
					}
				} while (!(choice == 1 || choice == 2));

				if (newUser.getUserId() > 0) {
					if (loginDAO.register(newUser)) {
						newUser.setUserId(loginDAO.seachByName(newUser.getUsername(), null, null).getUserId());
						if (customerDAO.createNewAccount(newUser.getUserId())) {
							System.out.println();
							System.out.println(newUser.getUsername()
									+ ", your account has been successfully created and is waiting approval");
						}
					}
				} else {
					System.err.println("Account creation failed");
				}
				break;

			case 8:
				System.out.println("\n---About Snowfall---");
				System.out.println("Version 1.0");
				System.out.println("Programed by Gideon Driver");
				System.out.println("Latest revision by Gideon Driver");
				System.out.println("");
				System.out.println(
						"Created for the purpose of testing the programer's knowledge about Java and their ability to code in Java.");
				break;

			case 9:
				System.out.println("               Thanks for using");
				System.out.println("   _____ ___ _ _____ _ _ _ _____ _____ _    _       ");
				System.out.println("  |  ___|   | |  _  | | | |  ___|  _  | |  | |      ");
				System.out.println("  |___  | | | | |_| | | | |  _| |  _  | |__| |__    ");
				System.out.println("  |_____|_|___|_____|_____|_|   |_| |_|____|____|   ");
				System.out.println();
				System.exit(0);
			default:
				System.out.println("###Invailid choice. Please enter (1-2), or (9) to EXIT.");
				break;

			}
			scanner = null;
		}
	}
}
