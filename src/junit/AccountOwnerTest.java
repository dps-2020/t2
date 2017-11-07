package junit;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import banksystem.Account;
import banksystem.AccountOwner;
import database.Database;


public class AccountOwnerTest {
	Database dataBase = Database.getInstance();

	@Before
	public void setUp() throws Exception {
		Database.setFileName("test.dat");
		dataBase.eraseFile();
		dataBase.load();
	}

	@Test
	public void nonExistingOwner() {
		assertEquals("Invalid Account Owner ID", AccountOwner.verifySavedOwnerId("O1001"));
	}
	
	@Test
	public void getNextIdTest() {
		assertEquals("O1001", AccountOwner.getNextId());
	}
	
	@Test
	public void getNextIdIntTest() {
		int pre = AccountOwner.getNextIdInt();
		AccountOwner accountOwner = new AccountOwner();
		accountOwner.put();
		String accountOwnerIdString = accountOwner.getId();
		Integer id = new Integer(accountOwnerIdString.substring(1,accountOwnerIdString.length()));
		assertEquals(pre, id.intValue());
	}
	
	
	@Test
	public void validateValidTest() {
		AccountOwner accountOwner = new AccountOwner();
		accountOwner.put();
		accountOwner.setName("ajunittestname");
		accountOwner.setPassword("Bad2P$");
		assertEquals("valid", accountOwner.validate());
	}
	
	@Test
	public void validateNameNoArgTest() {
		AccountOwner accountOwner = new AccountOwner();
		accountOwner.put();
		accountOwner.setName("ajunittestname");
		assertEquals("valid", accountOwner.validateName());
	}
	
	@Test
	public void validateValidConditionalTest() {
		AccountOwner accountOwner = new AccountOwner();
		accountOwner.put();
		accountOwner.setName("");
		accountOwner.setPassword("Bad2P$");
		assertEquals("Name cannot be empty", accountOwner.validate());
	}
	
	
	
	
	@Test
	public void validatePW2CharTest() {
		AccountOwner accountOwner = new AccountOwner();
		accountOwner.put();
		accountOwner.setName("ajunittestname");
		accountOwner.setPassword("2");
		assertEquals("Password minimum 2 characters", accountOwner.validate());
	}
	
	@Test
	public void validatePW6ChMaxTest() {
		AccountOwner accountOwner = new AccountOwner();
		accountOwner.put();
		accountOwner.setName("ajunittestname");
		accountOwner.setPassword("Bad2Password");
		assertEquals("Password maximum 6 characters", accountOwner.validate());
	}
	
	@Test
	public void validatePWSpacesTest() {
		AccountOwner accountOwner = new AccountOwner();
		accountOwner.put();
		accountOwner.setName("ajunittestname");
		accountOwner.setPassword("Bad2 ");
		assertEquals("Password cannot contain space(s)", accountOwner.validate());
	}
	
	@Test
	public void validatePW1NonAlphaTest() {
		AccountOwner accountOwner = new AccountOwner();
		accountOwner.put();
		accountOwner.setName("ajunittestname");
		accountOwner.setPassword("Bad2Pa");
		assertEquals("Password must contain at least 1 non-alphanumeric character", accountOwner.validate());
	}
	
	@Test
	public void validatePW1AlphaTest() {
		AccountOwner accountOwner = new AccountOwner();
		accountOwner.put();
		accountOwner.setName("ajunittestname");
		accountOwner.setPassword("$$$##@");
		assertEquals("Password must contain at least 1 alphanumeric character", accountOwner.validate());
	}
	
	@Test
	public void validateNameTest() {
		AccountOwner accountOwner = new AccountOwner();
		accountOwner.put();
		accountOwner.setName("ajunittestname");
		assertEquals("valid", accountOwner.validateName("ajunittestname"));
	}
	
	@Test
	public void validateNameValidTest() {
		assertEquals("valid", AccountOwner.validateName("ajunittestname"));
	}
	
	@Test
	public void validateNameEmptyTest() {
		assertEquals("Name cannot be empty", AccountOwner.validateName(""));
	}
	
	@Test
	public void validateName30ChTest() {
		assertEquals("Name must be less than 30 characters", AccountOwner.validateName("a123456789012345678901234567890123"));
	}
	
	@Test
	public void validateName1ChTest() {
		assertEquals("Name must be greater than 1 character", AccountOwner.validateName("a"));
	}
	
	@Test
	public void validatePasswordTest() {
		assertEquals("valid", AccountOwner.validatePassword("Bad2P$"));
	}
	
	@Test
	public void setNameTest() {
		AccountOwner accountOwner = new AccountOwner();
		accountOwner.put();
		String name = "ajunittestname";
		accountOwner.setName(name);
		assertEquals(name, accountOwner.getName());
		
	}
	
	@Test
	public void setPasswordTest() {
		AccountOwner accountOwner = new AccountOwner();
		accountOwner.put();
	    String password = "Bad2P$";
	    accountOwner.setPassword(password);
		assertEquals(password, accountOwner.getPassword());
	} 
	
	
	
	@Test
	public void existingOwner() {
		AccountOwner accountOwner = new AccountOwner();
		accountOwner.put();
		assertEquals("O1001", accountOwner.getId());
		assertEquals("valid", AccountOwner.verifySavedOwnerId("O1001"));
		assertEquals("Invalid Account Owner ID", AccountOwner.verifySavedOwnerId("O1002"));
	}
	
	@Test
	public void linkedAccount() {
		AccountOwner accountOwner = new AccountOwner();
		accountOwner.put();		
		Account account = new Account(accountOwner.getId(), "Checking", "100.00");
		account.put ();
		assertEquals("O1001", account.getOwnerId());
		Database.dump();
	}
	
	
	@Test
	public void validName() {
		String name = "First Last";
		Assert.assertEquals("valid", AccountOwner.validateName(name));
	}

	@Test
	public void minLength() {
		String name = "A";
		Assert.assertEquals("Name must be greater than 1 character",
				AccountOwner.validateName(name));
	}

	@Test
	public void maxLength() {
		String name = "012345678901234567890123456789N";
		Assert.assertEquals("Name must be less than 30 characters",
				AccountOwner.validateName(name));
	}
	
	@Test
	public void writeOwnerToDatabase() {
		AccountOwner owner = new AccountOwner("owner", "J$");
		owner.put();

		String ownerId = owner.getId();
		AccountOwner ownerWrittenToDatabase = AccountOwner.get(ownerId);

		Assert.assertEquals(ownerId, ownerWrittenToDatabase.getId());
		Assert.assertEquals("owner", ownerWrittenToDatabase.data.name);
		Assert.assertEquals("J$", ownerWrittenToDatabase.data.password);

	}
		
	@Test
	public void accountOwnerGetConditionalNullTest() {
		Assert.assertEquals(null, AccountOwner.get(null));
	}
	
	@Test
	public void isAccountOwnerAGoat()
	{
		String name = "Goat";
		Assert.assertEquals("valid", AccountOwner.validateName(name));
	}
	
	@Test
	public void isAccountOwnerAZombie()
	{
		String name = "Zombie";
		Assert.assertEquals("valid", AccountOwner.validateName(name));
	}
	

}
