package com.training.bms;

import java.util.ArrayList;
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
import com.training.bms.exceptions.ExcessiveBalanceException;
import com.training.bms.exceptions.InvalidUserInputException;
import com.training.bms.model.Customer;
import com.training.bms.model.EventLog;
import com.training.bms.model.Login;

public class CustomerMenu {

	Scanner scanner;
	int choice;
	int localAccountId;
	LoginDAO loginDAO = new LoginDAOImpl();
	CustomerDAO customerDAO = new CustomerDAOImpl();
	EmployeeDAO employeeDAO = new EmployeeDAOImpl();
	EventLogDAO eventLogDAO = new EventLogDAOImpl();
	List<Customer> accounts = new ArrayList<Customer>();
	List<Customer> transfers = new ArrayList<Customer>();

	public void customerMenu(Login login, String employeeName) {

		// declare local variables for input
		boolean customerMenuLoop = true;
		boolean subMenuLoop = true;
		int withdrawOrDeposit = 0;
		String[] accountView;

		while (customerMenuLoop) {


			// pull/update account info
			accounts = customerDAO.getAccounts(login.getUserId());
			if (accounts.size() == 0) {
				customerDAO.createNewAccount(login.getUserId());
				accounts = customerDAO.getAccounts(login.getUserId());
			}
			// format balances for quick view box
			String[] quickAccountView = customerDAO.listToPage(customerDAO.padBalances(24, true, true), 5);



			transfers = customerDAO.pendingTransfers(login.getUserId());
			int pendingTransferCount = transfers.size();
			String viewPendingTransfers;
			if (pendingTransferCount > 0) {
				viewPendingTransfers = "(" + pendingTransferCount + ")";
			} else {
				viewPendingTransfers = "   ";
			}

			System.out.println();
			System.out.println("-------CUSTOMER--MENU-------	==========Quickview==========");
			System.out.println("| 1. List Accounts         |	|| " + quickAccountView[0] + "||");
			System.out.println("| 2. Withdraw/Deposit      |	|| " + quickAccountView[1] + "||");
			System.out
					.println("| 3. Send/Accept Money " + viewPendingTransfers + " |	|| " + quickAccountView[2] + "||");
			System.out.println("| 4. Manage Accounts       |	|| " + quickAccountView[3] + "||");
			System.out.println("| 9. SIGN OUT              |	|| " + quickAccountView[4] + "||");
			System.out.println("----------------------------	=============================");

			try {
				scanner = new Scanner(System.in);
				choice = scanner.nextInt();
			} catch (Exception e) {
				choice = 0;
			}

			switch (choice) {
			// ========================================LIST
			// ACCOUNTS========================================
			case 1:
				System.out.println("\n--------------Accounts---------------");
				accountView = customerDAO.padBalances(24, false, true);
				for (int i = 0; i < accounts.size(); i++) {
					System.out.println("| "+accountView[i] + " | " + accounts.get(i).getAccountStatus()+" |");
				}
				System.out.println("-------------------------------------");
				break;

			// ========================================WITHDRAW/DEPOSIT========================================
			case 2:
				while (subMenuLoop) {
					System.out.println("\n---Withdraw/Deposit---");
					// withdraw/deposit . select account . amount

					System.out.println("1. Withdraw");
					System.out.println("2. Deposit");
					System.out.println("9. EXIT");

					try {
						scanner = new Scanner(System.in);
						choice = scanner.nextInt();
					} catch (Exception e) {
						choice = 0;
					}

					switch (choice) {
					case 1:
					case 2:
						withdrawOrDeposit = choice;
						List<Integer> selectedAccounts = customerDAO.selectLocalAccounts("Active");

						while (true) {
							System.out.println("\nSelect account:");
							accountView = customerDAO.padBalances(24, true, true);
							for (int i = 0; i < selectedAccounts.size(); i++) {
								System.out.println(accountView[i]);
							}

							try {
								scanner = new Scanner(System.in);
								choice = scanner.nextInt();
							} catch (Exception e) {
								choice = 0;
							}

							if (choice > 0 && choice <= selectedAccounts.size()) {
								int localAccountId = selectedAccounts.get(choice - 1);

								while (true) {
									if (withdrawOrDeposit == 1) {
										System.out.println("\nAmount to withdraw:");
									} else {
										System.out.println("\nAmount to deposit:");
									}

									String stringWithdrawDepositAmount;

									try {
										scanner = new Scanner(System.in);
										stringWithdrawDepositAmount = scanner.next();
									} catch (Exception e) {
										stringWithdrawDepositAmount = null;
									}

									long withdrawDepositAmount;
									try {
										withdrawDepositAmount = customerDAO
												.userEntryToLong(stringWithdrawDepositAmount);
									} catch (InvalidUserInputException e) {
										System.out.println();
										System.err.println(e.getMessage());
										break;
									} catch (ExcessiveBalanceException e) {
										System.out.println();
										System.err.println(e.getMessage());
										break;
									}

									if (withdrawDepositAmount > 0) {

										if (withdrawOrDeposit == 1) {
											try {
												if (customerDAO.withdraw(localAccountId, withdrawDepositAmount)) {
													eventLogDAO.logEvent(new EventLog(login.getUsername(), "Withdrew", withdrawDepositAmount, accounts.get(localAccountId).getAccountId(), employeeName, null));
													// Using double only to display currency, still not best practice.
													System.out.println("\nSuccessful withdraw of " + customerDAO
															.doubleToCurrency((double) withdrawDepositAmount / 100));
													subMenuLoop = false;
												}
											} catch (com.training.bms.exceptions.InsufficientBalanceException e) {
												System.out.println();
												System.err.println(e.getMessage());
											}
										} else if (withdrawOrDeposit == 2) {
											try {
												if (customerDAO.deposit(localAccountId, withdrawDepositAmount)) {
													eventLogDAO.logEvent(new EventLog(login.getUsername(), "Deposited", withdrawDepositAmount, accounts.get(localAccountId).getAccountId(), employeeName, null));
													System.out.println("\nSuccessful deposit of " + customerDAO
															.doubleToCurrency((double) withdrawDepositAmount / 100));
													subMenuLoop = false;
												}
											} catch (com.training.bms.exceptions.ExcessiveBalanceException e) {
												System.out.println();
												System.err.println(e.getMessage());
											}
										}
									} else {
										System.err.println("\nInvalid amount, can only enter positive numbers");
										// ### Throw
									}

									break;

								}

								break;
							} else {
								System.out.println();
								System.err.println("Invalid selection, please select an account number");
							}
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

			// ========================================TRANSFER
			// MONEY========================================
			case 3:
				while (subMenuLoop) {

//###
					pendingTransferCount = customerDAO.pendingTransfers(login.getUserId()).size();
					if (pendingTransferCount > 0) {
						viewPendingTransfers = "(" + pendingTransferCount + ")";
					} else {
						viewPendingTransfers = "   ";
					}

					System.out.println("\n---Transfer Money---");
					// send money to another account, can select by username (add note?)
					// accept/decline money, chose which account to add the transfer too

					System.out.println("1. Send money");
					System.out.println("2. Recieve money " + viewPendingTransfers);
					System.out.println("3. Seach for account");
					System.out.println("9. EXIT");

					try {
						scanner = new Scanner(System.in);
						choice = scanner.nextInt();
					} catch (Exception e) {
						choice = 0;
					}

					switch (choice) {
					case 1:
						System.out.println();
						System.out.println("Enter username of recipient:");
						scanner = new Scanner(System.in);
						String recipientName = scanner.next();
						if (recipientName.equalsIgnoreCase(login.getUsername())) {
							System.out.println();
							System.err.println("Cannot send money to same account");
							break;
						}
						int recipientId = loginDAO.seachByName(recipientName, "Customer", "Active").getUserId();
						if (recipientId == 0) {
							System.out.println();
							System.err.println("Name not found, please try again");
							break;
						}

						List<Integer> selectedAccounts = customerDAO.selectLocalAccounts("Active");

						System.out.println("\nSelect account:");
						accountView = customerDAO.padBalances(24, true, true);
						for (int i = 0; i < selectedAccounts.size(); i++) {
							System.out.println(accountView[i]);
						}

						try {
							scanner = new Scanner(System.in);
							choice = scanner.nextInt();
						} catch (Exception e) {
							choice = 0;
						}

						if (choice > 0 && choice <= selectedAccounts.size()) {
							int localAccountId = selectedAccounts.get(choice - 1);

							System.out.println("\nAmount to send:");

							String stringSendAmount;

							try {
								scanner = new Scanner(System.in);
								stringSendAmount = scanner.next();
							} catch (Exception e) {
								stringSendAmount = null;
							}

							long sendAmount;
							try {
								sendAmount = customerDAO.userEntryToLong(stringSendAmount);
							} catch (InvalidUserInputException e) {
								System.out.println();
								System.err.println(e.getMessage());
								break;
							}

							if (sendAmount > 0) {

								try {
									if (customerDAO.withdraw(localAccountId, sendAmount)) {

										try {
											if (customerDAO.sendMoney(login.getUserId(), recipientId, sendAmount)) {
												eventLogDAO.logEvent(new EventLog(login.getUsername(), "Transfered", sendAmount, accounts.get(localAccountId).getAccountId(), employeeName, null));
												// Using double only to display currency, still not best practice.
												System.out.println("\nSuccessfully sent "
														+ customerDAO.doubleToCurrency((double) sendAmount / 100)
														+ " to " + recipientName);
												subMenuLoop = false;
											}
										} catch (com.training.bms.exceptions.InsufficientBalanceException e) {
											System.out.println();
											System.err.println(e.getMessage());
										}

									}
								} catch (com.training.bms.exceptions.InsufficientBalanceException e) {
									System.out.println();
									System.err.println(e.getMessage());
								}

							} else {
								System.err.println("\nInvalid amount, can only enter positive numbers");
								// ### Throw
							}

						} else {
							System.err.println("Invalid selection, please select an account number");
						}

						break;
					case 2:

						// transfer money list
						while (true) {
							if (pendingTransferCount == 0) {
								System.out.println();
								System.err.println("No incoming transfers");
								break;
							}
							System.out.println();
							System.out.println("Select transfer to address");

							for (int i = 0; i < 5; i++) {
								try {
									Login queriedLogin = loginDAO.seachById(transfers.get(i).getUserId());
									System.out.println((i + 1) + ". " + queriedLogin.getUsername()
											+ " sent " + transfers.get(i).getFormattedAccountBalance());
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
							try {
								if (transfers.get(5).getUserId() > 0) {
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

							if (choice > 0 && (choice <= pendingTransferCount && choice <= 5)) {
								int selectedTransfer = (choice - 1);
								selectedAccounts = customerDAO.selectLocalAccounts("Active");

								while (true) {

									System.out.println("\nSelect account to deposit into:");
									accountView = customerDAO.padBalances(24, true, true);
									for (int i = 0; i < selectedAccounts.size(); i++) {
										System.out.println(accountView[i]);
									}

									try {
										scanner = new Scanner(System.in);
										choice = scanner.nextInt();
									} catch (Exception e) {
										choice = 0;
									}

									if (choice > 0 && choice <= selectedAccounts.size()) {
										int localAccountId = selectedAccounts.get(choice - 1);

										if (customerDAO.deposit(localAccountId,
												transfers.get(selectedTransfer).getAccountBalance())) {
											try {
												if (customerDAO.acceptMoneyTransfer(
														transfers.get(selectedTransfer).getAccountId())) {
													eventLogDAO.logEvent(new EventLog(login.getUsername(), "Recieved", transfers.get(selectedTransfer).getAccountBalance(), accounts.get(localAccountId).getAccountId(), employeeName, null));
													System.out.println();
													System.out.println("Successfully transfered "
															+ customerDAO.doubleToCurrency((double) transfers
																	.get(selectedTransfer).getAccountBalance() / 100));
													subMenuLoop = false;
												}
											} catch (Exception e) {
												// TODO: handle exception
											}
										}

										break;
									} else {
										System.out.println();
										System.err.println("Invalid selection, please select an account number");
									}

								}
								break;
							} else {
								System.out.println();
								System.err.println("Invalid selection, please select a number");
							}
						}
						break;
					case 3:

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
			// ========================================MANAGE
			// ACCOUNTS========================================
			case 4:
				while (subMenuLoop) {
					System.out.println("\n---Manage Accounts---");
					// create new account (limit 10?)
					// activate/deactivate/freeze accounts
					// delete account

					System.out.println("1. Create new account");
					System.out.println("2. Activate/Freeze accounts");
					System.out.println("3. Delete account");
					System.out.println("9. EXIT");

					try {
						scanner = new Scanner(System.in);
						choice = scanner.nextInt();
					} catch (Exception e) {
						choice = 0;
					}

					switch (choice) {
					case 1:
						if (accounts.size() < 10) {
							try {
								if (customerDAO.createNewAccount(login.getUserId())) {
									System.out.println("\nNew account created successfully");
									subMenuLoop = false;
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
						} else {
							System.out.println();
							System.err.println("Account limt of 10 reached");
						}
						break;
					case 2:

						System.out.println("\nSelect account to toggle freeze:");
						accountView = customerDAO.padBalances(24, false, true);
						for (int i = 0; i < accounts.size(); i++) {
							System.out.println(accountView[i] + "|" + accounts.get(i).getAccountStatus());
						}

						try {
							scanner = new Scanner(System.in);
							choice = scanner.nextInt();
						} catch (Exception e) {
							choice = 0;
						}

						if (choice > 0 && choice < accounts.size()) {
							int localAccountId = (choice - 1);
							boolean activate;
							if (accounts.get(localAccountId).getAccountStatus().equalsIgnoreCase("Frozen")) {
								activate = true;
							} else {
								activate = false;
							}

							try {
								if (customerDAO.changeAccountStatus(accounts.get(localAccountId).getAccountId(),
										activate)) {
									System.out.println("\nAccount(" + choice + ") status updated to ("
											+ (activate ? "Active" : "Frozen") + ") successfully");
									subMenuLoop = false;
									break;
								}
							} catch (Exception e) {
								// TODO: handle exception
							}

						} else {
							System.out.println();
							System.err.println("Invalid selection");
						}
						break;

					case 3:
						if (accounts.size() < 10) {
							System.out.println();
							System.err.println("Must retain (1) account");
							break;
						}
						List<Integer> selectedAccounts = customerDAO.selectLocalAccounts(null);

						System.out.println("\nSelect account to delete:");
						accountView = customerDAO.padBalances(24, false, true);
						for (int i = 0; i < selectedAccounts.size(); i++) {
							System.out.println(accountView[i]);
						}

						try {
							scanner = new Scanner(System.in);
							choice = scanner.nextInt();
						} catch (Exception e) {
							choice = 0;
						}

						if (choice > 0 && choice <= selectedAccounts.size()) {
							int localAccountId = selectedAccounts.get(choice - 1);

							if (accounts.get(localAccountId).getAccountBalance() <= 0) {
								try {
									if (customerDAO.deleteAccount(accounts.get(localAccountId).getAccountId())) {
										System.out.println("\nAccount(" + choice + ") deleted successfully");
										subMenuLoop = false;
										break;
									}
								} catch (Exception e) {
									// TODO: handle exception
								}
							} else {
								System.out.println();
								System.err.println("Account balance must be $0.00 to delete");
							}

						} else {
							System.out.println();
							System.err.println("Invalid selection");
						}
						break;
					case 9:
						System.out.println("\nReturning to menu");
						subMenuLoop = false;
						break;

					default:
						System.err.println("\nInvailid choice. Please enter (1-3) or (9) to EXIT");
						break;
					}

				}
				subMenuLoop = true;
				break;

			case 9:
				System.out.println("\nSigning out...");
				customerMenuLoop = false;
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
