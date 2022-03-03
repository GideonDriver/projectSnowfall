package com.training.bms.model;

import java.util.Objects;

public class Customer {
	private int userId;
	private int accountId;
	private long accountBalance;
	private String formattedAccountBalance;
	private String accountStatus;

	public Customer() {
		// TODO Auto-generated constructor stub
	}

	public Customer(int userId, int accountId, long accountBalance, String formattedAccountBalance,
			String accountStatus) {
		super();
		this.userId = userId;
		this.accountId = accountId;
		this.accountBalance = accountBalance;
		this.formattedAccountBalance = formattedAccountBalance;
		this.accountStatus = accountStatus;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public long getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(long accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getFormattedAccountBalance() {
		return formattedAccountBalance;
	}

	public void setFormattedAccountBalance(String formattedAccountBalance) {
		this.formattedAccountBalance = formattedAccountBalance;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountBalance, accountId, accountStatus, formattedAccountBalance, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return accountBalance == other.accountBalance && accountId == other.accountId
				&& Objects.equals(accountStatus, other.accountStatus)
				&& Objects.equals(formattedAccountBalance, other.formattedAccountBalance) && userId == other.userId;
	}

	@Override
	public String toString() {
		return "Customer [userId=" + userId + ", accountId=" + accountId + ", accountBalance=" + accountBalance
				+ ", formattedAccountBalance=" + formattedAccountBalance + ", accountStatus=" + accountStatus + "]";
	}
	
}
