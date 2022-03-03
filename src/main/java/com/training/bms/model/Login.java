package com.training.bms.model;

import java.util.Objects;

public class Login {
	private int userId;
	private String username;
	private String password;
	private String userType;
	private String userStatus;

	public Login() {
		// TODO Auto-generated constructor stub
	}

	public Login(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public Login(int userId, String username) {
		super();
		this.userId = userId;
		this.username = username;
	}

	public Login(int userId, String username, String password, String userType) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.userType = userType;
	}

	public Login(int userId, String username, String password, String userType, String status) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.userType = userType;
		this.userStatus = status;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String status) {
		this.userStatus = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(userType, password, userStatus, userId, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Login other = (Login) obj;
		return Objects.equals(userType, other.userType) && Objects.equals(password, other.password)
				&& Objects.equals(userStatus, other.userStatus) && userId == other.userId
				&& Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "Login [userId=" + userId + ", username=" + username + ", password=" + password + ", userType="
				+ userType + ", status=" + userStatus + "]";
	}

}
