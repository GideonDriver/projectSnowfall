package com.training.bms.dao;

import java.util.List;

import com.training.bms.model.Customer;

public interface CustomerDAO {

	public List<Customer> getAccounts(int userId);
	public String doubleToCurrency(double amount);
	public long userEntryToLong(String amount);
	
	public boolean withdraw(int localAccountId, long amount);
	public boolean deposit(int localAccountId, long amount);
	
	public boolean sendMoney(int senderId, int recipientId, long amount);
	public boolean acceptMoneyTransfer(int transferId);
	//public boolean declineMoneyTransfer(int transferId);
	public List<Customer> pendingTransfers(int userId);
	
	public boolean createNewAccount(int userId);
	public boolean changeAccountStatus(int userId, boolean activate);
	public boolean deleteAccount(int accountId);

	public String[] padBalances(int width, boolean active, boolean numbers);
	public String[] listToPage(String[] entries, int amountOnPage);

	public List<Integer> selectLocalAccounts(String status);
	
}