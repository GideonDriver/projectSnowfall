package com.training.bms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.training.bms.model.Customer;
import com.training.bms.utility.DBConnection;

public class CustomerDAOImpl implements CustomerDAO {

	List<Customer> accounts;
	List<Customer> transfers;
//	LoginDAO loginDAO = new LoginDAOImpl();
//	Login login = new Login();
	Customer customer;

	@Override
	public long userEntryToLong(String amount) {
		long full = 0;
		int decimal = 0;
		if (amount.lastIndexOf(".") >= 0) {
			try {
				full = Long.parseLong(amount.substring(0, amount.lastIndexOf(".")));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

			decimal = Integer.parseInt(amount.substring(amount.lastIndexOf(".") + 1, amount.length()));
			if (decimal < 10) {
				decimal *= 10;
			}
			if (decimal > 99) {
				throw new com.training.bms.exceptions.InvalidUserInputException(
						"Cannot have more than (2) digits after decimal place");
			}
		} else {
			full = Long.parseLong(amount);

		}

		if (full > 9999999999999L) {
			throw new com.training.bms.exceptions.ExcessiveTransferException(
					"Cannot process more than 10 trillion dollars at a time.");
		}
		return ((full * 100) + decimal);
	}

	@Override
	public String doubleToCurrency(double amount) {
		NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
		String formatted = (defaultFormat.format(amount));
		return formatted;
	}

	@Override
	public List<Customer> getAccounts(int userId) {
		Connection connection = DBConnection.getConnection();
		accounts = new ArrayList<Customer>();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(
					"select userId,accountid,accountbalance,status from snowfallAccounts where userId = ? order by accountid");
			statement.setInt(1, userId);

			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Customer customer = new Customer();
				customer.setUserId(result.getInt(1));
				customer.setAccountId(result.getInt(2));
				customer.setAccountBalance(result.getLong(3));
				// money is stored at +2 decimal places to keep cents
				customer.setFormattedAccountBalance(doubleToCurrency((double) result.getLong(3) / 100));

				customer.setAccountStatus(result.getString(4));
				accounts.add(customer);
			}
			try {
				result.close();
			} catch (Exception e) {
				/* Ignored */ }
			try {
				statement.close();
			} catch (Exception e) {
				/* Ignored */ }
			try {
				connection.close();
			} catch (Exception e) {
				/* Ignored */ }
		} catch (SQLException e) {
			// e.printStackTrace();
		}
		return accounts;
	}

	@Override
	public boolean withdraw(int localAccountId, long amount) {
		Connection connection = DBConnection.getConnection();
		PreparedStatement statement;
		try {
			if (amount > accounts.get(localAccountId).getAccountBalance()) {
				throw new com.training.bms.exceptions.InsufficientBalanceException(
						"Cannot withdraw more than current account balance.");
			} else {
				statement = connection
						.prepareStatement("update snowfallAccounts set accountbalance = ? where accountid = ?;");
				statement.setLong(1, (accounts.get(localAccountId).getAccountBalance() - amount));
				statement.setInt(2, accounts.get(localAccountId).getAccountId());

				int rows = statement.executeUpdate();
			}

			try {
				statement.close();
			} catch (Exception e) {
				/* Ignored */ }
			try {
				connection.close();
			} catch (Exception e) {
				/* Ignored */ }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean deposit(int localAccountId, long amount) {
		Connection connection = DBConnection.getConnection();
		PreparedStatement statement;
		try {
			long maxBal = 999999999999999L;
			if ((amount + accounts.get(localAccountId).getAccountBalance()) > maxBal) {
				throw new com.training.bms.exceptions.ExcessiveBalanceException(
						"Cannot have more than 10 trillion dollars in your account.");
			} else {
				statement = connection
						.prepareStatement("update snowfallAccounts set accountbalance = ? where accountid = ?;");
				statement.setLong(1, (accounts.get(localAccountId).getAccountBalance() + amount));
				statement.setInt(2, accounts.get(localAccountId).getAccountId());

				int rows = statement.executeUpdate();
			}

			try {
				statement.close();
			} catch (Exception e) {
				/* Ignored */ }
			try {
				connection.close();
			} catch (Exception e) {
				/* Ignored */ }

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean sendMoney(int senderId, int recipientId, long amount) {
		Connection connection = DBConnection.getConnection();
		PreparedStatement statement;
		try {

			statement = connection
					.prepareStatement("insert into snowfallTransfers values (default, ? , ?, ?, default);");
			statement.setInt(1, senderId);
			statement.setInt(2, recipientId);
			statement.setLong(3, amount);

			int rows = statement.executeUpdate();

			try {
				statement.close();
			} catch (Exception e) {
				/* Ignored */ }
			try {
				connection.close();
			} catch (Exception e) {
				/* Ignored */ }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean acceptMoneyTransfer(int transferId) {
		Connection connection = DBConnection.getConnection();
		boolean success = false;
		PreparedStatement statement;
		try {
			statement = connection
					.prepareStatement("update snowfallTransfers set status = 'Accepted' where transferId = ?");
			statement.setInt(1, transferId);

			int rows = statement.executeUpdate();
			success = (rows > 0);

			try {
				statement.close();
			} catch (Exception e) {
				/* Ignored */ }
			try {
				connection.close();
			} catch (Exception e) {
				/* Ignored */ }

		} catch (SQLException e) {
			// e.printStackTrace();
		}
		return success;
	}

	@Override
	public List<Customer> pendingTransfers(int userId) {
		Connection connection = DBConnection.getConnection();
		transfers = new ArrayList<Customer>();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(
					"select transferId,senderId,amount from snowfallTransfers where recipientId = ? and status = 'Pending' order by transferid");
			statement.setInt(1, userId);

			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Customer transfer = new Customer();
				transfer.setAccountId(result.getInt(1));
				transfer.setUserId(result.getInt(2));
				transfer.setAccountBalance(result.getLong(3));
				transfer.setFormattedAccountBalance(doubleToCurrency((double) result.getLong(3) / 100));
				transfers.add(transfer);
			}
			try {
				result.close();
			} catch (Exception e) {
				/* Ignored */ }
			try {
				statement.close();
			} catch (Exception e) {
				/* Ignored */ }
			try {
				connection.close();
			} catch (Exception e) {
				/* Ignored */ }
		} catch (SQLException e) {
			// e.printStackTrace();
		}
		return transfers;
	}

	@Override
	public boolean createNewAccount(int userId) {
		Connection connection = DBConnection.getConnection();
		boolean success = false;
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("insert into snowfallAccounts values (default, ?, 0, default)");
			statement.setInt(1, userId);

			int rows = statement.executeUpdate();
			success = (rows > 0);

			try {
				statement.close();
			} catch (Exception e) {
				/* Ignored */ }
			try {
				connection.close();
			} catch (Exception e) {
				/* Ignored */ }

		} catch (SQLException e) {
			// e.printStackTrace();
		}
		return success;
	}

	@Override
	public boolean changeAccountStatus(int userId, boolean activate) {
		Connection connection = DBConnection.getConnection();
		boolean success = false;
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("update snowfallAccounts set status = ? where accountid = ?;");
			if (activate) {
				statement.setString(1, "Active");
			} else {
				statement.setString(1, "Frozen");
			}
			statement.setInt(2, userId);

			int rows = statement.executeUpdate();
			success = (rows > 0);

			try {
				statement.close();
			} catch (Exception e) {
				/* Ignored */ }
			try {
				connection.close();
			} catch (Exception e) {
				/* Ignored */ }

		} catch (SQLException e) {
			// e.printStackTrace();
		}
		return success;
	}

	@Override
	public boolean deleteAccount(int accountId) {
		Connection connection = DBConnection.getConnection();
		boolean success = false;
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("delete from snowfallAccounts where accountid = ?");
			statement.setInt(1, accountId);

			int rows = statement.executeUpdate();
			success = (rows > 0);

			try {
				statement.close();
			} catch (Exception e) {
				/* Ignored */ }
			try {
				connection.close();
			} catch (Exception e) {
				/* Ignored */ }

		} catch (SQLException e) {
			// e.printStackTrace();
		}
		return success;
	}

	@Override
	public List<Integer> selectLocalAccounts(String status) {
		List<Integer> selectedAccounts = new ArrayList<Integer>(10);
		for (int i = 0; i < accounts.size(); i++) {
			if (status == null) {
				selectedAccounts.add(i);
			} else if (accounts.get(i).getAccountStatus().equalsIgnoreCase(status)) {
				selectedAccounts.add(i);
			}
		}
		return selectedAccounts;
	}

	@Override
	public String[] padBalances(int width, boolean active, boolean numbers) {
		// Id active accounts
		List<Integer> selectedAccounts = new ArrayList<Integer>(10);
		if (active) {
			selectedAccounts = selectLocalAccounts("Active");
		} else {
			selectedAccounts = selectLocalAccounts(null);
		}

		// Pad rows so quick-view box looks correct
		String[] format = new String[selectedAccounts.size()];
		for (int i = 0; i < selectedAccounts.size(); i++) {
			StringBuilder sb = new StringBuilder();
			if (numbers) {
				sb.append((i + 1) + ": ");
			}
			while (sb
					.length() < (width - accounts.get(selectedAccounts.get(i)).getFormattedAccountBalance().length())) {
				sb.append(' ');
			}
			sb.append(accounts.get(selectedAccounts.get(i)).getFormattedAccountBalance());
			format[i] = sb.toString();
		}
		return format;
	}

	@Override
	public String[] listToPage(String[] entries, int amountOnPage) {
		String[] page = new String[(amountOnPage)];
		for (int l = 0; (l < entries.length) && (l < amountOnPage); l++) {
			page[l] = entries[l];
		}
		int length = entries[0].length();

		// test cases: (3,4) (4,4) (5,4)
		if (entries.length < amountOnPage) {
			// return all entries and add empty entries
			for (int i = amountOnPage; i > entries.length; i--) {
				page[i - 1] = " ";
			}
		} else if (amountOnPage < entries.length) {
			// set last visible entry to ... and return
			page[(amountOnPage) - 1] = "...";
		}

		// pad empty entries and "..." entry
		for (int j = 0; j < (amountOnPage); j++) {
			String spaces = page[j];
			for (int k = spaces.length(); k < length; k++) {
				spaces = spaces + " ";
			}
			page[j] = spaces;
		}

		return page;
	}

}
