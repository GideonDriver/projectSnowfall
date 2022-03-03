package com.training.bms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.training.bms.model.Login;
import com.training.bms.utility.DBConnection;

public class LoginDAOImpl implements LoginDAO {

	public boolean validate(Login login) {
		Connection connection = DBConnection.getConnection();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("select * from snowfallLogin where username = ? and password = ? ");
			statement.setString(1, login.getUsername());
			statement.setString(2, login.getPassword());

			ResultSet result = statement.executeQuery();
			result.next();
			login.setUserId(result.getInt(1));
			login.setUsername(result.getString(2));
			login.setPassword(result.getString(3));
			login.setUserType(result.getString(4));
			login.setUserStatus(result.getString(5));
			
		    try { result.close(); } catch (Exception e) { /* Ignored */ }
		    try { statement.close(); } catch (Exception e) { /* Ignored */ }
		    try { connection.close(); } catch (Exception e) { /* Ignored */ }
		    
			return true;
		} catch (SQLException e) {
			// e.printStackTrace();
			return false;
		}
	}

	public boolean register(Login login) {
		Connection connection = DBConnection.getConnection();
		PreparedStatement statement = null;
		int rows = 0;
		try {
			if (login.getUserId() == 1) {
				statement = connection.prepareStatement("insert into snowfallLogin values(default,?,?,?,'Pending')");
				statement.setString(1, login.getUsername());
				statement.setString(2, login.getPassword());
				statement.setString(3, login.getUserType());
			} else {
				statement = connection.prepareStatement("insert into snowfallLogin values(?,?,?,?,'Pending')");
				statement.setInt(1, login.getUserId());
				statement.setString(2, login.getUsername());
				statement.setString(3, login.getPassword());
				statement.setString(4, login.getUserType());
			}

			rows = statement.executeUpdate();

		    try { statement.close(); } catch (Exception e) { /* Ignored */ }
		    try { connection.close(); } catch (Exception e) { /* Ignored */ }

		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (rows == 0)
			return false;
		else
			return true;
	}

	public boolean usernameTaken(String username) {
		Connection connection = DBConnection.getConnection();
		boolean usernameExists = false;
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("select * from snowfallLogin where username = ? ");
			statement.setString(1, username);

			ResultSet result = statement.executeQuery();
			usernameExists = result.next();

		    try { result.close(); } catch (Exception e) { /* Ignored */ }
		    try { statement.close(); } catch (Exception e) { /* Ignored */ }
		    try { connection.close(); } catch (Exception e) { /* Ignored */ }

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return usernameExists;
	}

	@Override
	public boolean employeeIdTaken(int userId) {
		Connection connection = DBConnection.getConnection();
		boolean idTaken = false;
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("select * from snowfallLogin where userid = ? ");
			statement.setInt(1, userId);

			ResultSet result = statement.executeQuery();
			idTaken = result.next();

		    try { result.close(); } catch (Exception e) { /* Ignored */ }
		    try { statement.close(); } catch (Exception e) { /* Ignored */ }
		    try { connection.close(); } catch (Exception e) { /* Ignored */ }

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idTaken;
	}

	@Override
	public Login seachByName(String username, String userType, String status) {
		Connection connection = DBConnection.getConnection();
		int userId = 0;
		PreparedStatement statement;
		Login queriedLogin = new Login();
		StringBuilder modifyStatment = new StringBuilder("select * from snowfallLogin where username = ?");

		if (userType != null) {
			modifyStatment.append(" and accountType = ?");
		}
		if (status != null) {
			modifyStatment.append(" and status = ?");
		}
		try {
			statement = connection.prepareStatement(modifyStatment.toString());
			statement.setString(1, username);
		    try { statement.setString(2, userType); } catch (Exception e) {}
		    try { statement.setString(3, status); } catch (Exception e) {}

			ResultSet result = statement.executeQuery();
			result.next();
			queriedLogin.setUserId(result.getInt(1));
			queriedLogin.setUsername(result.getString(2));
			queriedLogin.setPassword(result.getString(3));
			queriedLogin.setUserType(result.getString(4));
			queriedLogin.setUserStatus(result.getString(5));

		    try { result.close(); } catch (Exception e) {}
		    try { statement.close(); } catch (Exception e) {}
		    try { connection.close(); } catch (Exception e) {}

		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return queriedLogin;
	}

	@Override
	public Login seachById(int userId) {
		Connection connection = DBConnection.getConnection();
		Login queriedLogin = new Login();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("select * from snowfallLogin where userId = ?");
			statement.setInt(1, userId);

			ResultSet result = statement.executeQuery();
			result.next();
			queriedLogin.setUserId(result.getInt(1));
			queriedLogin.setUsername(result.getString(2));
			queriedLogin.setPassword(result.getString(3));
			queriedLogin.setUserType(result.getString(4));
			queriedLogin.setUserStatus(result.getString(5));

		    try { result.close(); } catch (Exception e) { /* Ignored */ }
		    try { statement.close(); } catch (Exception e) { /* Ignored */ }
		    try { connection.close(); } catch (Exception e) { /* Ignored */ }

		} catch (SQLException e) {
			//e.printStackTrace();
		}
		return queriedLogin;
	}

}
