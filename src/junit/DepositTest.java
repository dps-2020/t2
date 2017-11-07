package junit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import banksystem.Account;
import banksystem.AccountOwner;
import banksystem.Deposit;
import banksystem.PasswordManager;
import database.Database;

public class DepositTest {
final String GOOD_PASSWORD = "M$09230w";
	Database dataBase = Database.getInstance();

	@Before
	public void setUp() throws Exception {
		Database.setFileName("test.dat");
		dataBase.eraseFile();
		dataBase.load();
	}

	@After
	public void tearDown() throws Exception {
	}
@Test
public void getNullTest() {
	Assert.assertNull( Deposit.get("bananaface"));
}

@Test
public void getDepositTest() {
	String retrieveId = Deposit.getNextId();
	new Deposit ("bananaface", "passionnose", "guavatongue").put();
	Deposit deposit = Deposit.get(retrieveId);
	Assert.assertEquals("guavatongue",deposit.getDepositAmount());
	Assert.assertEquals("passionnose", deposit.getAccountId());
	Assert.assertEquals("bananaface", deposit.getOwnerId());
}
	@Test
	public void UpdateDeposit() {
		// Testing deposits update and calculate properly. UAT 3.1
		Deposit newDeposit = new Deposit("O1001", "A1001", "100.00");
		Account newAccount = new Account("O1001", "Checking", "50.00");
		newAccount.put();
		AccountOwner newAccountOwner = new AccountOwner("Michael", "M$09230w");
		newAccountOwner.put();
		Assert.assertEquals("Invalid Password", newDeposit.updateBalance("P$2222"));
		Assert.assertEquals("valid", newDeposit.updateBalance("M$09230w"));
		Assert.assertEquals("150.00", newAccount.getBalance());
		newDeposit.put();
		// @TODO Fix this test, deposits should count once
		Assert.assertEquals("valid", newDeposit.updateBalance("M$09230w"));
		Assert.assertEquals("250.00", newAccount.getBalance());

	}

@Test
public void getIdTest() {
	Database.getInstance().resetInMemory();
	Deposit newDeposit = new Deposit("O1001", "A1001", "100.00");
	newDeposit.put();
	Assert.assertEquals ("D1001", newDeposit.getId());
	}
	
@Test
public void setDepositAmountTest() {
	Deposit newDeposit = new Deposit("O1001", "A1001", "100.00");
	newDeposit.setDepositAmount("mangoEar");
	Assert.assertEquals ("mangoEar", newDeposit.getDepositAmount());
	}

@Test
public void setAmountIdTest() {
	Deposit newDeposit = new Deposit("O1001", "A1001", "100.00");
	newDeposit.setAccountId("appleHead");
	Assert.assertEquals ("appleHead", newDeposit.getAccountId());
	}
 
@Test
public void setOwnerIdTest() {
	Deposit newDeposit = new Deposit("O1001", "A1001", "100.00");
	newDeposit.setOwnerId("pearHair");
	Assert.assertEquals ("pearHair", newDeposit.getOwnerId());
	}

@Test
public void getIdAsIntTest() {
	Database.getInstance().resetInMemory();
	Deposit newDeposit = new Deposit("O1001", "A1001", "100.00");
	Assert.assertEquals (new Integer (1001), newDeposit.getIdAsInt());
	
}
	

@Test
public void updateBalanceInvalidAccountOwnerTest() {
	Assert.assertEquals("Invalid Account Owner ID", new Deposit("bananaface", "passionnose", "guavatongue").updateBalance(GOOD_PASSWORD));
}

@Test
public void updateBalanceInvalidAccountTest() {
	AccountOwner newAccountOwner = new AccountOwner("Michael", "M$09230w");
	newAccountOwner.put();
	Assert.assertEquals("Invalid Account ID", new Deposit("O1001", "passionnose", "guavatongue").updateBalance(GOOD_PASSWORD));

}

	@Test
	public void DepositNotNegative() {
		// Testing deposits do not contain a negative amount. UAT 3.2
		Deposit deposit = new Deposit();
		// Assert.assertEquals("valid", deposit.validateDepositAmount("-110"));
		Assert.assertEquals("Deposit amount cannot be negative", deposit.validateDepositAmount("-110"));
	}

	@Test
	public void notSpaces() {
		// Testing a new deposit for correct values. No spaces allowed. UAT 3.3
		Deposit newDeposit = new Deposit("O1001", "A1001", "    ");
		Assert.assertEquals("Deposit amount cannot be space(s)",
				newDeposit.validateDepositAmount(newDeposit.getDepositAmount()));
		Assert.assertEquals("Deposit amount cannot be space(s)", newDeposit.validateDepositAmount(" "));
	}

	@Test
	public void notEmpty() {
		// Testing a new deposit for correct values. No empty values allowed.
		// UAT 3.4
		Deposit newDeposit = new Deposit("O1001", "A1001", "");
		Assert.assertEquals("Deposit amount cannot be empty",
				newDeposit.validateDepositAmount(newDeposit.getDepositAmount()));
	}

	@Test
	public void testDepositAmountCannotBeZero() {
		// Testing deposit amounts cannot be any variation of 0. UAT 3.5
		Deposit deposit1 = new Deposit("O1002", "A1004", "1.00");
		// Assert.assertEquals("valid", deposit1.validateDepositAmount("0.00"))
		// ;
		Assert.assertEquals("Deposit amount cannot be zero", deposit1.validateDepositAmount("0.00"));
		Assert.assertEquals("Deposit amount cannot be zero", deposit1.validateDepositAmount("00"));
		// Assert.assertEquals("valid", deposit1.validateDepositAmount("0")) ;
		Assert.assertEquals("Deposit amount cannot be zero", deposit1.validateDepositAmount("0"));
		// Assert.assertEquals("Deposit amount cannot be zero",
		// deposit1.validateDepositAmount("1.00")) ;
		Assert.assertEquals("valid", deposit1.validateDepositAmount("1.00"));
	}

	@Test
	public void testDepositAmountIsNumeric() {
		// Testing to make sure only numeric input is allowed, and not any
		// string input. UAT 3.6

		Deposit deposit1 = new Deposit("O1002", "A1004", "1.00");
		// Assert.assertEquals("valid", deposit1.validateDepositAmount("23w")) ;
		Assert.assertEquals("Deposit amount must be numeric", deposit1.validateDepositAmount("23w"));
		// Assert.assertEquals("Deposit amount must be numeric",
		// deposit1.validateDepositAmount("23.00"));
		Assert.assertEquals("valid", deposit1.validateDepositAmount("23.00"));
	}

	@Test
	public void testDepositAmountMustBeDollarsAndCents() {
		// Testing to make sure numeric data fits precision of dollar values, to
		// two decimals. UAT 3.9

		Deposit deposit1 = new Deposit("O1002", "A1004", "50.00");
		// Assert.assertEquals("valid",
		// deposit1.validateDepositAmount("1.234"));
		Assert.assertEquals("Amount must be dollars and cents", deposit1.validateDepositAmount("1.234"));
		// Assert.assertEquals("Amount must be dollars and cents",
		// deposit1.validateDepositAmount("1.23"));
		Assert.assertEquals("valid", deposit1.validateDepositAmount("1.23"));
	}

	@Test
	public void testAccountOwnerId() {
		AccountOwner accountOwner1 = new AccountOwner("John Doe", "PW1@");
		accountOwner1.put();
		Account account1 = new Account("O1001", "Checking", "1.00");
		account1.put();
		Deposit deposit1 = new Deposit("O1001", "A1004", "1.00");
		// Assert.assertEquals("Invalid Account Owner ID",
		// accountOwner1.validateOwnerId("O1001"));
		Assert.assertEquals("valid", accountOwner1.verifySavedOwnerId("O1001"));
		Assert.assertEquals("Invalid Account Owner ID", accountOwner1.verifySavedOwnerId("O1002"));
		// Assert.assertEquals("valid", accountOwner1.validateOwnerId("O1002"));
	}

	@Test
	public void testAccountOwnerPassword() {
		AccountOwner accountOwner1 = new AccountOwner("John Doe", "PW1@");
		accountOwner1.put();
		Account account1 = new Account("O1002", "Checking", "1.00");
		account1.put();
		Deposit deposit1 = new Deposit("O1002", "A1004", "1.00");
		// Assert.assertEquals("valid",
		// (PasswordManager.authenticate(accountOwner1.getPassword(),
		// "P$2222")));
		Assert.assertEquals("Invalid Password", (PasswordManager.authenticate(accountOwner1.getPassword(), "P$2222")));
		// Assert.assertEquals("Invalid Password",
		// (PasswordManager.authenticate(accountOwner1.getPassword(), "PW1@")));
		Assert.assertEquals("valid", (PasswordManager.authenticate(accountOwner1.getPassword(), "PW1@")));

	}
}// End DepositTest