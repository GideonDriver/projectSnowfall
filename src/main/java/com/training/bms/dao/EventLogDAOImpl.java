package com.training.bms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.training.bms.model.Customer;
import com.training.bms.model.EventLog;
import com.training.bms.model.Login;
import com.training.bms.utility.DBConnection;

public class EventLogDAOImpl implements EventLogDAO {

	@Override
	public boolean logEvent(EventLog event) {
		Connection connection = DBConnection.getConnection();
		PreparedStatement statement = null;
		int rows = 0;
		try {
			statement = connection.prepareStatement("insert into snowfallEvents values(default,?,?,?,?,?,?,default)");

			statement.setString(1, event.getActingUser());
			statement.setString(2, event.getActionDescription());
			statement.setLong(3, event.getAmount());
			statement.setInt(4, event.getFromAccount());
			statement.setString(5, event.getControlledBy());
			statement.setString(6, event.getTransferedToName());

			rows = statement.executeUpdate();

			try {
				statement.close();
			} catch (Exception e) {
				/* Ignored */ }
			try {
				connection.close();
			} catch (Exception e) {
				/* Ignored */ }

		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (rows == 0)
			return false;
		else
			return true;
	}

	@Override
	public List<EventLog> getEvents() {
		Connection connection = DBConnection.getConnection();
		CustomerDAO customerDAO = new CustomerDAOImpl();
		List<EventLog> eventList = new ArrayList<EventLog>();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("select * from snowfallEvents order by logEventsId ASC");

			ResultSet result = statement.executeQuery();
			while (result.next()) {
				EventLog eventDetails = new EventLog();
				eventDetails.setLogEventsId(result.getInt(1));
				eventDetails.setActingUser(result.getString(2));
				eventDetails.setActionDescription(result.getString(3));
				eventDetails.setAmount(result.getLong(4));
				eventDetails.setFormattedAmount(customerDAO.doubleToCurrency((double) result.getLong(4) / 100));;
				eventDetails.setFromAccount(result.getInt(5));
				if (result.getString(6) == null) {
					eventDetails.setControlledBy("");
				} else {
					eventDetails.setControlledBy(result.getString(6));
				}
				if (result.getString(7) == null) {
					eventDetails.setTransferedToName("");
				} else {
					eventDetails.setTransferedToName(result.getString(7));
				}
				eventDetails.setTimeOfTransaction(result.getString(8));
				eventList.add(eventDetails);
			}

			try {result.close();} catch (Exception e) {}
			try {statement.close();} catch (Exception e) {}
			try {connection.close();} catch (Exception e) {}

		} catch (SQLException e) {
			// e.printStackTrace();
		}
		return eventList;
	}

}
