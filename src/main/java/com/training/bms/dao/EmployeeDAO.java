package com.training.bms.dao;

import java.util.List;

import com.training.bms.model.Customer;
import com.training.bms.model.Login;

public interface EmployeeDAO {

	public List<Login> getPendingAccounts();
	public boolean updateAccountStatus(int userId, String status);
	
	public Login listAccount(String accountIdOrUsername);
	
	public List<Login> wildcardSearch(String keyword);
	
}