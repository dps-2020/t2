package uat;

import banksystem.Account;


public class AccountWithStatus extends Account {

	private static final long serialVersionUID = 1L;

	public AccountWithStatus(String id, String accountType, String balance) {
		super(id,accountType,balance);
	}

	public AccountWithStatus(String result) {
		super();
		status=result;
	}

	String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
