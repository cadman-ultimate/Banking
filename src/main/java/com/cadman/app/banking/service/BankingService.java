package com.cadman.app.banking.service;

import java.util.List;

import com.cadman.app.banking.model.Person;
import com.cadman.app.banking.response.Response;

public interface BankingService {

	Response<String> getBalance(String accNumber);
	
	Response<String> addAmount(double amount, String accNumber);
	
	Response<String> withdrawAmount(String accNumber, double amount);
	
	Response<String> transferFunds(double amount, String fromAccountNo, String toAccountNo);
	
	Response<String> addAccount(Person person);
	
	Response<List<Person>> getAccounts();
	
}
