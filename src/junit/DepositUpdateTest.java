package junit;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import database.Database;
import banksystem.Account;
import banksystem.AccountOwner;
import banksystem.Deposit;

public class DepositUpdateTest {

	Database dataBase = Database.getInstance();

	@Before
	public void setUp() throws Exception {
		Database.setFileName("test.dat");
		dataBase.eraseFile();
		dataBase.load();
	}

	@Test
	public void UpdateDeposit() {
		Account newAccount = new Account("O1001", "Checking", "50.00");
		newAccount.put();

		AccountOwner newAccountOwner = new AccountOwner("Michael", "M$09230w");
		newAccountOwner.put();

		Deposit newDeposit = new Deposit("O1001", "A1001", "100.00");

		Assert.assertEquals("valid", newDeposit.updateBalance("M$09230w"));
		Assert.assertEquals("150.00", newAccount.getBalance());

		newDeposit.put();

	}
	
	@Test
	public void UpdateDepositBadOwner() {
		Database.getInstance().resetInMemory();

		AccountOwner accountOwner = new AccountOwner("Michael", "M$09230w");
		accountOwner.put();
		String accountOwnerId = accountOwner.getId();
				
		Account account = new Account(accountOwnerId, "Checking", "50.00");
		account.put();
		String accountId = account.getId();
		
		AccountOwner depositOwner = new AccountOwner("Roko basilisk", "I♡Pupπes");
		depositOwner.put();
		String depositOwnerId = depositOwner.getId();

		Deposit newDeposit = new Deposit(depositOwnerId, accountId, "100.00");
		// Show that the account is not valid for the owner
		Assert.assertEquals("Invalid Account ID", newDeposit.updateBalance("I♡Pupπes"));
		
	}

}
