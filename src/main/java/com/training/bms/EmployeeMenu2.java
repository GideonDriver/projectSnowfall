package com.training.bms;

import java.util.Scanner;

import com.training.bms.model.Login;

public class EmployeeMenu2 extends MainMenu {

	Scanner scanner;
	int choice;

	public void employeeMenu(Login login) {

		// declare local variables for input
		boolean loop1 = true;

		while (loop1) {

			System.out.println();
			System.out.println("------ACCOUNT MENU------");
			System.out.println("| 1. New Accounts      |");
			System.out.println("| 2. Customer Accounts |");
			System.out.println("| 3. Transaction Log   |");
			System.out.println("| 9. SIGN OUT          |");
			System.out.println("------------------------");

			try {
				scanner = new Scanner(System.in);
				choice = scanner.nextInt();
			} catch (Exception e) {
				choice = 0;
			}

			switch (choice) {
			case 1:
				System.out.println("\n---New Accounts---");
				break;

			case 2:
				System.out.println("\n---Customer Accounts---");
				break;
				
			case 3:
				System.out.println("\n---Transaction Log---");
				break;

			case 9:
				System.out.println("\nSigning out...");
				loop1 = false;
				break;
			default:
				System.out.println("\n###Invailid choice. Please enter (1-3), or (9) to EXIT.");
				break;

			}
			scanner = null;
		}
	}
}
