package banksystem;

import java.io.Serializable;

import database.Database;

public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	public static String prefix = "A";

	public String id;

	static Database database = Database.getInstance();

	public AccountData data = new AccountData();

	public Account(String ownerId, String accountType, String balance) {
		data.ownerId = ownerId;
		data.accountType = accountType;
		data.balance = balance;
	}

	public Account() {
	}

	public String validate() {
		return ( validateOwnerIdAccountTypeBalance( getOwnerId(),  getAccountType(),  getBalance()));
	}

	public static String validateOwnerIdAccountTypeBalance(String ownerId, String accountType,
			String balance) {
		if (!AccountOwner.validateOwnerId(ownerId).equals("valid")) {
			return (AccountOwner.verifySavedOwnerId(ownerId));
		} else if (!validateAccountType(accountType).equals("valid")) {
			return (validateAccountType(accountType));
		} else if (!validateBalance(balance).equals("valid")) {
			return (validateBalance(balance));
		} else {
			return ("valid");
		}
	}

	public static String validateAccountExists(String accountId) {
		if (Database.exists(accountId)) {
			return ("valid");
		} else {
			return ("Invalid Account ID");
		}
	}

	public String validateAccountType() {
		return (validateAccountType(data.accountType));
	}

	public static String validateAccountType(String accountType) {
		if ((accountType.equals("Checking")) || (accountType.equals("Savings"))) {
			return ("valid");
		} else {
			return ("Account Type invalid");
		}
	}

	public String validateBalance() {
		return (validateBalance(data.balance));
	}

	public static String validateBalance(String balance) {
		if (balance.length() == 0) {
			return ("Balance cannot be empty");
		}
		if (balance.trim().length() == 0) {
			return ("Balance cannot be blank");
		}
		if (Utilities.isNegative(balance)) {
			return ("Balance cannot be negative");
		}
		if (!Utilities.isNumeric(balance)) {
			return ("Balance must be numeric");
		}
		if (!Utilities.isMoney(balance)) {
			return ("Amount must be dollars and cents");
		}
		return ("valid");
	}
	


	public String add(String amount) {
		if (!validateBalance(amount).equals("valid")) {
			return (validateBalance(amount));
		}
		long amountToAdd = Utilities.toCents(amount);
		long currentBalance = Utilities.toCents(getBalance());
		long newBalance = currentBalance + amountToAdd;
		setBalance(Utilities.toMoney(newBalance));
		return ("valid");

	}

	public String subtract(String s) {
		if (!validateBalance(s).equals("valid")) {
			return (validateBalance(s));
		}
		long amountToSubtract = Utilities.toCents(s);
		long currentBalance = Utilities.toCents(getBalance());
		long newBalance = currentBalance - amountToSubtract;
		setBalance(Utilities.toMoney(newBalance));
		return ("valid");
	}

	public String getAccountType() {
		return data.accountType;
	}

	public String getBalance() {
		return data.balance;
	}

	public String getFormattedBalance() {
		return ("$" + data.balance);
	}

	public String getId() {
		return id;
	}

	public static String getNextId() {
		return (Database.getNextIdString(prefix));
	}

	public Integer getIdAsInt() {
		return (Database.getNextIdInt(prefix));
	}

	public String getOwnerId() {
		return data.ownerId;
	}

	public void setAccountType(String accountType) {
		this.data.accountType = accountType;
	}

	public void setBalance(String balance) {
		this.data.balance = balance;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOwnerId(String ownerId) {
		this.data.ownerId = ownerId;
	}

	public void put() {
		setId(Database.getNextIdString(prefix));
		database.put(Database.getNextIdString(prefix), data);
	}

	public static Account get(String accountId) {
		AccountData accountData = (AccountData) Database.get(accountId);
		if (accountData == null) {
			return (null);
		}
		Account account = new Account();
		account.data = accountData;
		account.setId(accountId);
		return (account);
	}

	public static int getNextIdInt() {
		return (Database.getNextIdInt(prefix));
	}

	@Override
	public String toString() {
		return "Account [data=" + data + "]";
	}

}
