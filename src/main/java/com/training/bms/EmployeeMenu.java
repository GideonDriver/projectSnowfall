package com.training.bms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.training.bms.dao.CustomerDAO;
import com.training.bms.dao.CustomerDAOImpl;
import com.training.bms.dao.EmployeeDAO;
import com.training.bms.dao.EmployeeDAOImpl;
import com.training.bms.dao.EventLogDAO;
import com.training.bms.dao.EventLogDAOImpl;
import com.training.bms.dao.LoginDAO;
import com.training.bms.dao.LoginDAOImpl;
import com.training.bms.exceptions.InvalidUserInputException;
import com.training.bms.model.Customer;
import com.training.bms.model.EventLog;
import com.training.bms.model.Login;

public class EmployeeMenu {

	Scanner scanner;
	int choice;
	int localAccountId;
	Login login;
	LoginDAO loginDAO = new LoginDAOImpl();
	CustomerDAO customerDAO = new CustomerDAOImpl();
	EmployeeDAO employeeDAO = new EmployeeDAOImpl();
	EventLogDAO eventLogDAO = new EventLogDAOImpl();
	List<Customer> accounts = new ArrayList<Customer>();
	List<Customer> transfers = new ArrayList<Customer>();

	public void employeeMenu(Login login) {

		// declare local variables for input
		boolean employeeMenuLoop = true;
		boolean subMenuLoop = true;
		boolean subSubMenuLoop = true;
		int withdrawOrDeposit = 0;
		String[] accountView;

		while (employeeMenuLoop) {

			List<Login> pendingAccounts = employeeDAO.getPendingAccounts();
			int pendingAccountsCount = pendingAccounts.size();
			String viewPendingAccounts;
			if (pendingAccountsCount > 0) {
				viewPendingAccounts = "(" + pendingAccountsCount + ")";
			} else {
				viewPendingAccounts = "   ";
			}

			// view all accounts and elements ###testing purposes
//			Iterator<Login> iterator = pendingAccounts.iterator();
//			while (iterator.hasNext()) {
//				Login temp = iterator.next();
//				System.out.println(temp);
//			}

			System.out.println();
			System.out.println("------Employee MENU------");
			System.out.println("| 1. Customer Accounts  |");
			System.out.println("| 2. New Accounts " + viewPendingAccounts + "   |");
			System.out.println("| 3. Transaction Log    |");
			System.out.println("| 9. SIGN OUT           |");
			System.out.println("-------------------------");

			try {
				scanner = new Scanner(System.in);
				choice = scanner.nextInt();
			} catch (Exception e) {
				choice = 0;
			}

			switch (choice) {
			// ========================================CUSTOMER
			// ACCOUNTS========================================
			case 1:
				while (subMenuLoop) {
					System.out.println("\n---Customer Accounts---");
					System.out.println("1. View account");
					System.out.println("2. Search for account");
					System.out.println("9. EXIT");

					try {
						scanner = new Scanner(System.in);
						choice = scanner.nextInt();
					} catch (Exception e) {
						choice = 0;
					}

					switch (choice) {
					case 1:
						System.out.println("\nEnter User Id/Username: ");

						scanner = new Scanner(System.in);
						String accountIdOrUsername = scanner.next();

						Login queriedLogin = employeeDAO.listAccount(accountIdOrUsername);

						if (queriedLogin.getUserId() > 0) {

							while (subSubMenuLoop) {

								queriedLogin = employeeDAO.listAccount(accountIdOrUsername);
								List<Customer> queriedAccounts = customerDAO.getAccounts(queriedLogin.getUserId());

								System.out.println("\n--------------------------------------------------------");
								System.out.printf("|%-9s|%-20s|%-10s|%-12s|", "Id #", "Username", "Type", "Status");
								System.out.println();
								System.out.println("========================================================");
								System.out.printf("|%-9s|%-20s|%-10s|%-12s|", queriedLogin.getUserId(),
										queriedLogin.getUsername(), queriedLogin.getUserType(),
										queriedLogin.getUserStatus());
								System.out.println("\n--------------------------------------------------------");
								for (int i = 0; i < queriedAccounts.size(); i++) {
									System.out.printf("|%-9s|%31s|%-12s|", queriedAccounts.get(i).getAccountId(),
											queriedAccounts.get(i).getFormattedAccountBalance(),
											queriedAccounts.get(i).getAccountStatus());
									System.out.println();
								}
								System.out.println("--------------------------------------------------------");

								System.out.println("\n---Viewing " + queriedLogin.getUsername() + "'s Account---");
								System.out.println("1. Login as " + queriedLogin.getUsername());
								System.out.println("2. Deactivate/Activate");
								System.out.println("8. Switch user");
								System.out.println("9. EXIT");

								try {
									scanner = new Scanner(System.in);
									choice = scanner.nextInt();
								} catch (Exception e) {
									choice = 0;
								}

								switch (choice) {
								case 1:
									if (queriedLogin.getUserType().equals("Customer")) {
										// ###Invoke method for customer menu
										CustomerMenu customerMenuObject = new CustomerMenu();
										customerMenuObject.customerMenu(queriedLogin, login.getUsername());
									} else {
										System.out.println();
										System.err.println("Connnot login as another employee");
									}
									break;
								case 2:

									String oppositeStatus = null;
									String oppositeStatusMenu = null;
									if (queriedLogin.getUserStatus().equals("Active")) {
										oppositeStatus = "Deactivated";
										oppositeStatusMenu = "Deactivate";
									} else if (queriedLogin.getUserStatus().equals("Deactivated")
											|| queriedLogin.getUserStatus().equals("Pending")
											|| queriedLogin.getUserStatus().equals("Denied")) {
										oppositeStatus = "Active";
										oppositeStatusMenu = "Activate";
									}

									System.out.println("\n---Account Status---");
									System.out.println("1. " + oppositeStatusMenu + " account");
									System.out.println("9. CANCEL");

									try {
										scanner = new Scanner(System.in);
										choice = scanner.nextInt();
									} catch (Exception e) {
										choice = 0;
									}

									if (choice == 1) {

										try {
											if (employeeDAO.updateAccountStatus(queriedLogin.getUserId(),
													oppositeStatus)) {
												System.out.println("\nSuccessfully " + oppositeStatusMenu.toLowerCase()
														+ "d " + queriedLogin.getUsername() + "'s account");
												break;
											}
										} catch (Exception e) {
											// TODO: handle exception
										}

									} else if (choice == 9) {

									} else {
										System.out.println();
										System.err.println("Invailid choice");
									}

									break;
								case 8:
									System.out.println("\nReturning user selection menu");
									subSubMenuLoop = false;
									break;
								case 9:
									System.out.println("\nReturning to menu");
									subSubMenuLoop = false;
									subMenuLoop = false;
									break;

								default:
									System.out.println();
									System.err.println("Invailid choice. Please enter (1-2) or (8-9) to EXIT");
									break;
								}
							}
							subSubMenuLoop = true;
						} else {
							System.out.println();
							System.err.println("Invailid User Id or Username");
							break;
						}

						break;
					case 2:

						System.out.println("\nEnter search keyword:");

						scanner = new Scanner(System.in);
						String keyword = scanner.next();

						List<Login> matchedResults = employeeDAO.wildcardSearch(keyword);

						if (matchedResults.size() > 0) {
							System.out.println();
							System.out.printf("%-9s|%-20s", "Id #", "Username");
							System.out.println("\n------------------");
							for (int i = 0; (i < matchedResults.size()) && (i < 10); i++) {
								System.out.printf("%-9s|%-20s", matchedResults.get(i).getUserId(),
										matchedResults.get(i).getUsername());
								System.out.println();
							}
							if (matchedResults.size() > 10) {
								System.err.println("Cannot display all search results, please narrow search");
							}
						} else {
							System.out.println();
							System.err.println("No results");
							break;
						}

						break;
					case 9:
						System.out.println("\nReturning to menu");
						subMenuLoop = false;
						break;

					default:
						System.err.println("\nInvailid choice. Please enter (1-2) or (9) to EXIT");
						break;
					}
				}
				subMenuLoop = true;
				break;

			// ========================================NEW
			// ACCOUNTS========================================
			case 2:

				System.out.println();
				System.out.println("Select account to address");

				for (int i = 0; i < 5; i++) {
					try {
						System.out.println((i + 1) + ". " + pendingAccounts.get(i).getUserId() + " | "
								+ pendingAccounts.get(i).getUserType() + " | " + pendingAccounts.get(i).getUsername());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

				try {
					if (pendingAccounts.get(5).getUserId() > 0) {
						System.out.println("...");
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

				try {
					scanner = new Scanner(System.in);
					choice = scanner.nextInt();
				} catch (Exception e) {
					choice = 0;
				}

				if (choice > 0 && (choice <= pendingAccountsCount && choice <= 5)) {
					int selectedAccount = (choice - 1);

					while (true) {

						System.out.println("\nSelect:");
						System.out.println("1. Approve");
						System.out.println("2. Reject");

						try {
							scanner = new Scanner(System.in);
							choice = scanner.nextInt();
						} catch (Exception e) {
							choice = 0;
						}

						if (choice == 1 || choice == 2) {
							String newStatus;
							if (choice == 1) {
								newStatus = "Active";
							} else {
								newStatus = "Denied";
							}

							try {
								if (employeeDAO.updateAccountStatus(pendingAccounts.get(selectedAccount).getUserId(),
										newStatus)) {
									System.out.println();
									if (choice == 1) {
										System.out.println("Successfully activated "
												+ pendingAccounts.get(selectedAccount).getUsername() + "'s account");
									} else {
										System.out.println("Successfully rejected "
												+ pendingAccounts.get(selectedAccount).getUsername() + "'s account");
									}
								}
							} catch (Exception e) {
								// e.printStackTrace();
							}

							subMenuLoop = false;
							break;
						} else {
							System.out.println();
							System.err.println("Invalid selection, please select (1-2)");
						}

					}
					break;
				} else {
					System.out.println();
					System.err.println("Invalid selection, please select an account");
				}
				break;

			// ========================================LIST EVENT LOGS
			// ========================================
			case 3:
				List<EventLog> eventLog = eventLogDAO.getEvents();
				int pageSize = (15-1);
				int page = 1;
				int pageTop = 0;
				int pageBottom = 14;
				
				while (subMenuLoop) {

					System.out.println("\n===============================================================(Page "+page+")===============================================================");
					System.out.printf("|%-20s|%-10s|%-20s|%-10s|%-20s|%-20s|%-26s|", "Username", "Action", "Amount", "Account ID", "Controlled By", "Transfered To", "Timestamp");
					System.out.println("\n======================================================================================================================================");
					for (int i = pageTop; (i < eventLog.size()) && (i < pageBottom); i++) {
						System.out.printf("|%-20s|%-10s|%20s|%-10s|%-20s|%-20s|%-26s|", eventLog.get(i).getActingUser(), eventLog.get(i).getActionDescription(), eventLog.get(i).getFormattedAmount(), eventLog.get(i).getFromAccount(), eventLog.get(i).getControlledBy(), eventLog.get(i).getTransferedToName(), eventLog.get(i).getTimeOfTransaction());
						System.out.println();
					}
					System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
					System.out.println("(1. Prev. Page|2. Next Page|9. EXIT)");
					
					try {
						scanner = new Scanner(System.in);
						choice = scanner.nextInt();
					} catch (Exception e) {
						choice = 0;
					}
					
					switch (choice) {
					case 1:
						if (pageTop == 0) {
							System.out.println();
							System.err.println("No previous page");
						} else {
							pageTop -= pageSize;
							pageBottom -= pageSize;
							page--;
						}
						break;
					case 2:
						if (pageBottom >= eventLog.size()) {
							System.out.println();
							System.err.println("No next page");
						} else {
							pageTop += pageSize;
							pageBottom += pageSize;
							page++;
						}
						break;
					case 9:
						subMenuLoop = false;
						break;
					default:
						System.out.println();
						System.err.println("Invailid choice. Please enter (1-2), or (9) to EXIT.");
						break;
					}
					
				}
				subMenuLoop = true;
				break;
			case 9:
				System.out.println("\nSigning out...");
				employeeMenuLoop = false;
				break;
			default:
				System.out.println();
				System.err.println("Invailid choice. Please enter (1-3), or (9) to EXIT.");
				break;

			}
			scanner = null;
		}
	}
}
