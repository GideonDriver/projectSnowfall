package com.training.bms.dao;

import java.util.List;

import com.training.bms.model.Customer;
import com.training.bms.model.EventLog;
import com.training.bms.model.Login;

public interface EventLogDAO {

	public boolean logEvent(EventLog event);
	public List<EventLog> getEvents();
	
}