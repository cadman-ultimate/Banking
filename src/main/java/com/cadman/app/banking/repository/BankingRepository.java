package com.cadman.app.banking.repository;

import java.util.List;

import com.cadman.app.banking.entity.AccountEntity;

public interface BankingRepository {
	
	void addAccount(AccountEntity acc);
	
	List<AccountEntity> getListOfAccounts();
	
	AccountEntity getAccount(String accNumber);
	
	void updateBalance(double balance, String accNumber) throws Exception;
	
	void transferFunds(double fromBalance, double toBalance, String fromAccountNo, String toAccountNo);

}
