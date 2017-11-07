package junit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import banksystem.Account;
import banksystem.Utilities;
import database.Database;

public class AccountTest {

	Database database = Database.getInstance();

	@Before
	public void setUp() throws Exception {
		Database.setFileName("test.dat");
		database.eraseFile();
		database.load();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void saveOwnerTypeAmount() {		
		Account account = new Account("O1001", "Checking", "100.00");
		String validationString = account.validate();
		account.put();
		Assert.assertEquals("valid", validationString);
	}
	
	
	@Test
	public void saveInvalidOwnert() {		
		Account account = new Account("OO101", "Checking", "100.00");
		String validationString = account.validate();
		account.put();
		Assert.assertEquals("Invalid Account Owner ID", validationString);
	}
	
	@Test
	public void saveInvalidType() {		
		Account account = new Account("O1001", "Checkings", "100.00");
		String validationString = account.validate();
		account.put();
		Assert.assertEquals("Account Type invalid", validationString);
	}
	
	
	@Test
	public void saveInvalidBalance() {		
		Account account = new Account("O1001", "Checking", "100.000");
		String validationString = account.validate();
		account.put();
		Assert.assertEquals("Amount must be dollars and cents", validationString);
	}
	
	
	
	@Test
	public void testAccountId() {

		Account account = new Account("ownerId", "Checking", "100.00");
		String nextAccountId = Account.getNextId();
		Assert.assertEquals("A1001", nextAccountId);
		account.put();
		Database.dump("Write it to the database:");
		account = Account.get(nextAccountId);
		System.out.println(account);
		Assert.assertEquals(nextAccountId, account.getId());

		String afterAddIndex = Account.getNextId();
		Integer startingId = new Integer(nextAccountId.substring(1));
		Integer endingId = new Integer(afterAddIndex.substring(1));
		Assert.assertEquals(startingId.intValue(), endingId.intValue() - 1);
		Assert.assertEquals(endingId, account.getIdAsInt());
		Assert.assertEquals(1002,Account.getNextIdInt());
	}
	
	@Test
	public void setOwnerId() {
		Account account = new Account("O1001", "Checking", "50");
		account.setOwnerId("Morgul the friendly drelb");
		Assert.assertEquals("Morgul the friendly drelb", account.getOwnerId());
	}
	
	@Test
	public void setAccountType() {
		Account account = new Account("O1001", "Checking", "50");
		account.setAccountType("Checking");
		Assert.assertEquals("Checking", account.getAccountType());
	}

	@Test
	public void checkAccountType() {

		Account account = new Account("O1001", "Checking", "50");
		Assert.assertEquals("valid", account.validateAccountType());
		account = new Account("O1001", "Savings", "50");
	
		Assert.assertEquals("valid", account.validateAccountType());
		account = new Account("O1001", "Money Market", "50");

		Assert.assertEquals("Account Type invalid",
				account.validateAccountType());
	}
	
	

	@Test
	public void checkMoney() {

		Account account = new Account("O1001", "Checking", "50");
		Assert.assertEquals("valid", account.validateBalance());
		Assert.assertEquals("valid", Account.validateBalance("50"));
		account = new Account("O1001", "Savings", "x");
		Assert.assertEquals("Balance must be numeric", account.validateBalance());
		Assert.assertEquals("Balance must be numeric", Account.validateBalance("x"));

	}

	@Test
	public void addMoney() {
		Account account = new Account("O1001", "Checking", "50");
		Assert.assertEquals("valid",account.add("100"));
		Assert.assertEquals("150.00",account.getBalance());
	}
	


	@Test
	public void add() {
		Account account = new Account("O1001", "Checking", "50");
		Assert.assertEquals("valid",account.add("100"));
		Assert.assertEquals("150.00",account.getBalance());
	}
	
	@Test
	public void subMoney() {
		Account account = new Account("O1001", "Checking", "50");
		Assert.assertEquals("valid",account.subtract("50"));
		Assert.assertEquals("0.00",account.getBalance());
	}
	
	@Test
	public void subtract() {
		Account account = new Account("O1001","Checking","50");
		Assert.assertEquals("valid", account.subtract("25.00"));
		Assert.assertEquals("25.00", account.getBalance());
	
	}
	
	@Test
	public void negativeBalance() {
		String balance = "-50";
		Assert.assertEquals("Balance cannot be negative",Account.validateBalance(balance) );		
	}

	@Test
	public void zeroBalance() {
		String balance = "0";
		Assert.assertEquals("valid",Account.validateBalance(balance) );		
	}
	
	@Test
	public void dollarsAndCents() {
		String balance = "50.25";
		Assert.assertEquals("valid",Account.validateBalance(balance) );		
	}

	@Test
	public void getFormattedBalance() {
		String expected = "$50";
		Account account = new Account("O1001", "Checking", "50");
		Assert.assertEquals(expected,account.getFormattedBalance() );		
		}
/*
	@Test
	public void getFormattedBalance() {
		String balance = "50.25";
		Assert.assertEquals("$valid","$" + Account.validateBalance(balance));
		String expected ="$50.25";
		Assert.assertEquals(expected, "$" + balance);
	}
*/
	@Test
	public void accountDoesNotExistMessage() {
		Assert.assertEquals("Account should not exist", "Invalid Account ID", Account.validateAccountExists("A1001"));
	}
	
}
