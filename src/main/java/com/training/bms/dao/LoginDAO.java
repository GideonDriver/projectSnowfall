package com.training.bms.dao;

import com.training.bms.model.Login;

public interface LoginDAO {
	public boolean register(Login login);
	public boolean validate(Login login);
	public boolean usernameTaken(String username);
	public boolean employeeIdTaken(int userId);
	public Login seachByName(String username, String userType, String status);
	public Login seachById(int userId);
}
