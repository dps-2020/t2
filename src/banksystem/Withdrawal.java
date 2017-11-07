package banksystem;

import java.io.Serializable;

import database.Database;

public class Withdrawal implements Serializable {

	private static final long serialVersionUID = 1L;

	public static String prefix = "W";

	public String id;

	static Database database = Database.getInstance();

	public WithdrawalData data = new WithdrawalData();

	public Withdrawal(String ownerId, String accountId, String withdrawalAmount) {
		this.data.ownerId = ownerId;
		this.data.accountId = accountId;
		this.data.withdrawalAmount = withdrawalAmount;
	}

	public Withdrawal() { }
	

	public String getOwnerId() {
		return data.ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.data.ownerId = ownerId;
	}

	public String getAccountId() {
		return data.accountId;
	}

	public void setAccountId(String accountId) {
		this.data.accountId = accountId;
	}

	public String getWithdrawalAmount() {
		return data.withdrawalAmount;
	}

	public void setWithdrawalAmount(String withdrawalAmount) {
		this.data.withdrawalAmount = withdrawalAmount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static String getNextId() {
		return (Database.getNextIdString(prefix));
	}

	public Integer getIdAsInt() {
		return (Database.getNextIdInt(prefix));
	}

	public void put() {
		setId(Database.getNextIdString(prefix));
		database.put(Database.getNextIdString(prefix), data);
	}

	public static Withdrawal get(String withdrawalId) {
		WithdrawalData withdrawalData = (WithdrawalData) Database.get(withdrawalId);
		if (withdrawalData == null) {
			return (null);
		}
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.data = withdrawalData;
		withdrawal.setId(withdrawalId);
		return (withdrawal);
	}
	public String validateWithdrawalAmount(String withdrawalAmount) {
		if (withdrawalAmount.equals(""))
			return ("Withdrawal amount cannot be empty");
		if ((withdrawalAmount.charAt(0)) == (' '))
			return ("Withdrawal amount cannot be blank");
		if (Utilities.isNegative(withdrawalAmount))
			return ("Withdrawal amount cannot be negative");
		if (!Utilities.isNumeric(withdrawalAmount)) {
		 	return ("Withdrawal amount must be numeric");}
		if (!Utilities.isMoney(withdrawalAmount)) {
			return ("Amount must be dollars and cents");}
		long cents = 0;
		cents = Utilities.toCents(withdrawalAmount);
	 	if ((cents) == 0)
		    return ("Withdrawal amount cannot be zero");
	 	Account newAccount = Account.get(this.data.accountId);
	 	long centsWithdrawalAmount = 0;
		long centsBalance = 0;
		centsWithdrawalAmount = Utilities.toCents(withdrawalAmount);
		centsBalance = Utilities.toCents(newAccount.getBalance());
		System.out.println("The value of balance is: " + centsBalance);
		System.out.println("The value of withdrawal is: " + centsWithdrawalAmount);
		if (centsWithdrawalAmount > centsBalance)
			return ("Withdrawal amount cannot be greater than balance");
		
		return ("valid");
	}

	public String validate(String password) {
		// TODO:
		return ("String validate(String password)");
	}

	public static String validate(String ownerId, String accountId, String password, String withdrawalAmount) {
		// TODO:
		return ("String validate(String ownerId, String accountId, String password, String withdrawalAmount) ");
	}

	public String updateBalance(String password) {
		AccountOwner newAccountOwner = AccountOwner.get(this.data.ownerId);
		System.out.println ( this.data.ownerId + " " +  newAccountOwner );
		if (AccountOwner.verifySavedOwnerId(this.data.ownerId).equals("valid"))
			if (PasswordManager.authenticate(password, newAccountOwner.getPassword()) == "valid")
			{
				if (Account.validateAccountExists(this.data.accountId).equals("valid"))
				{
					Account newAccount = Account.get(this.data.accountId);
					if (newAccount.getOwnerId().equals(this.data.ownerId))
					{
						if (validateWithdrawalAmount(this.data.withdrawalAmount).equals("valid"))
						{
							newAccount.subtract(this.data.withdrawalAmount);
							newAccount.put();
							return ("valid");
						}
						else
							return (validateWithdrawalAmount(this.data.withdrawalAmount));
					}
					else
						return ("Invalid Account ID");						
				}
				else
					return (Account.validateAccountExists(this.data.accountId));
			}
			else
				return (AccountOwner.authenticate(password, this.data.ownerId));
		else
			return (AccountOwner.verifySavedOwnerId(this.data.ownerId));
	}
	
}
	
