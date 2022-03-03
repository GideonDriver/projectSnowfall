package com.training.bms.model;

import java.util.Objects;

public class EventLog {
	private int logEventsId;
	private String actingUser;
	private String actionDescription;
	private long amount;
	private String formattedAmount;
	private int fromAccount;
	private String controlledBy;
	private String transferedToName;
	private String timeOfTransaction;

	public EventLog() {
		// TODO Auto-generated constructor stub
	}

	public EventLog(String actingUser, String actionDescription, long amount, int fromAccount, String controlledBy,
			String transferedToName) {
		super();
		this.actingUser = actingUser;
		this.actionDescription = actionDescription;
		this.amount = amount;
		this.fromAccount = fromAccount;
		this.controlledBy = controlledBy;
		this.transferedToName = transferedToName;
	}

	public EventLog(int logEventsId, String actingUser, String actionDescription, long amount, int fromAccount,
			String controlledBy, String transferedToName, String timeOfTransaction) {
		super();
		this.logEventsId = logEventsId;
		this.actingUser = actingUser;
		this.actionDescription = actionDescription;
		this.amount = amount;
		this.fromAccount = fromAccount;
		this.controlledBy = controlledBy;
		this.transferedToName = transferedToName;
		this.timeOfTransaction = timeOfTransaction;
	}

	public EventLog(int logEventsId, String actingUser, String actionDescription, String formattedAmount, int fromAccount,
			String controlledBy, String transferedToName, String timeOfTransaction) {
		super();
		this.logEventsId = logEventsId;
		this.actingUser = actingUser;
		this.actionDescription = actionDescription;
		this.formattedAmount = formattedAmount;
		this.fromAccount = fromAccount;
		this.controlledBy = controlledBy;
		this.transferedToName = transferedToName;
		this.timeOfTransaction = timeOfTransaction;
	}
	
	public int getLogEventsId() {
		return logEventsId;
	}

	public void setLogEventsId(int logEventsId) {
		this.logEventsId = logEventsId;
	}

	public String getActingUser() {
		return actingUser;
	}

	public void setActingUser(String actingUser) {
		this.actingUser = actingUser;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getFormattedAmount() {
		return formattedAmount;
	}

	public void setFormattedAmount(String formattedAmount) {
		this.formattedAmount = formattedAmount;
	}

	public int getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(int fromAccount) {
		this.fromAccount = fromAccount;
	}

	public String getControlledBy() {
		return controlledBy;
	}

	public void setControlledBy(String controlledBy) {
		this.controlledBy = controlledBy;
	}

	public String getTransferedToName() {
		return transferedToName;
	}

	public void setTransferedToName(String transferedToName) {
		this.transferedToName = transferedToName;
	}

	public String getTimeOfTransaction() {
		return timeOfTransaction;
	}

	public void setTimeOfTransaction(String timeOfTransaction) {
		this.timeOfTransaction = timeOfTransaction;
	}

	@Override
	public int hashCode() {
		return Objects.hash(actingUser, actionDescription, amount, controlledBy, formattedAmount, fromAccount,
				logEventsId, timeOfTransaction, transferedToName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventLog other = (EventLog) obj;
		return Objects.equals(actingUser, other.actingUser)
				&& Objects.equals(actionDescription, other.actionDescription) && amount == other.amount
				&& Objects.equals(controlledBy, other.controlledBy)
				&& Objects.equals(formattedAmount, other.formattedAmount) && fromAccount == other.fromAccount
				&& logEventsId == other.logEventsId && Objects.equals(timeOfTransaction, other.timeOfTransaction)
				&& Objects.equals(transferedToName, other.transferedToName);
	}

	@Override
	public String toString() {
		return "EventLog [logEventsId=" + logEventsId + ", actingUser=" + actingUser + ", actionDescription="
				+ actionDescription + ", amount=" + amount + ", formattedAmount=" + formattedAmount + ", fromAccount="
				+ fromAccount + ", controlledBy=" + controlledBy + ", transferedToName=" + transferedToName
				+ ", timeOfTransaction=" + timeOfTransaction + "]";
	}


}
