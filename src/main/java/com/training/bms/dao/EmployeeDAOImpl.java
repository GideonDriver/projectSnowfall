package com.training.bms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.training.bms.model.Customer;
import com.training.bms.model.Login;
import com.training.bms.utility.DBConnection;

public class EmployeeDAOImpl implements EmployeeDAO {

	LoginDAO loginDAO = new LoginDAOImpl();

	@Override
	public List<Login> getPendingAccounts() {
		Connection connection = DBConnection.getConnection();
		List<Login> pendingAccounts = new ArrayList<Login>();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("select * from snowfallLogin where status = 'Pending' ");

			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Login pendingAccount = new Login();
				pendingAccount.setUserId(result.getInt(1));
				pendingAccount.setUsername(result.getString(2));
				pendingAccount.setPassword(result.getString(3));
				pendingAccount.setUserType(result.getString(4));
				pendingAccount.setUserStatus(result.getString(5));
				pendingAccounts.add(pendingAccount);
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
		return pendingAccounts;
	}

	@Override
	public boolean updateAccountStatus(int userId, String status) {
		Connection connection = DBConnection.getConnection();
		PreparedStatement statement;
		boolean success = false;
		try {
			statement = connection.prepareStatement("update snowfallLogin set status = ? where userid = ?;");
			statement.setString(1, status);
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
	public Login listAccount(String accountIdOrUsername) {
		Login queriedLogin = new Login();
		int userId = 0;
		try {
			userId = Integer.parseInt(accountIdOrUsername);
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (userId > 9999999 && userId < 100000000) {
			queriedLogin = loginDAO.seachById(userId);
		} else {
			queriedLogin = loginDAO.seachByName(accountIdOrUsername, null, null);
		}

		return queriedLogin;
	}

	@Override
	public List<Login> wildcardSearch(String keyword) {
		Connection connection = DBConnection.getConnection();
		PreparedStatement statement;
		List<Login> matchedResults = new ArrayList<Login>();
		
		keyword = "%"+keyword+"%";

		try {
			statement = connection
					.prepareStatement("select userid,username from snowfallLogin where username ilike ?");
			statement.setString(1, keyword);

			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Login matchedResult = new Login();
				matchedResult.setUserId(result.getInt(1));
				matchedResult.setUsername(result.getString(2));
				matchedResults.add(matchedResult);
			}

			try {result.close();} catch (Exception e) {}
			try {statement.close();} catch (Exception e) {}
			try {connection.close();} catch (Exception e) {}

		} catch (SQLException e) {
			// e.printStackTrace();
		}
		return matchedResults;
	}

}
