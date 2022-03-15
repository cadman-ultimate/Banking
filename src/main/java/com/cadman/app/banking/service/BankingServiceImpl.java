package com.cadman.app.banking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cadman.app.banking.entity.AccountEntity;
import com.cadman.app.banking.exception.IncompleteFundsException;
import com.cadman.app.banking.model.Person;
import com.cadman.app.banking.repository.BankingRepository;
import com.cadman.app.banking.response.Response;

@Service
public class BankingServiceImpl implements BankingService {
	
	private static final String WITHDRAW_FUNDS_FAILED_WITH_ERROR = "Withdraw funds failed with error: ";
	private static final String TRANSFER_FUNDS_FAILED_WITH_ERROR = "Transfer funds failed with error: ";
	private static final String INADEQUATE_FUNDS_ERROR_MSG = "Not enough funds to complete task!";
	private static final String NEW_BALANCE = "Your new balance: ";

	Logger logger = LoggerFactory.getLogger(BankingServiceImpl.class);

	@Autowired
	BankingRepository repository;

	public Response<String> getBalance(String accNumber) {
		
		Response<String> response = new Response<String>();
		try {
			AccountEntity entity = repository.getAccount(accNumber);
			response.setStatus(true);
			response.setMsg("Your remaining balance: €" + entity.getBalance());
		} catch (Exception ex) {
			logger.error("Get Balance failed with error: " + ex.getMessage() + ex.getCause());
			response.setStatus(false);
			response.setMsg("Error: Could not retrieve balance, check account exists.");
		}
		return response;

	}

	@Override
	@Transactional
	public Response<String> addAmount(double amount, String accNumber) {

		Response<String> response = new Response<String>();
		double currentBalance = 0.0;
		double newBalance = 0.0;

		try {
			currentBalance = repository.getAccount(accNumber).getBalance();
			newBalance = currentBalance + amount;
			repository.updateBalance(newBalance, accNumber);
			response.setStatus(true);
			response.setMsg(NEW_BALANCE + newBalance);
		} catch (Exception ex) {
			logger.error("Add funds failed with error: " + ex.getMessage() + ex.getCause());
			response.setStatus(false);
			response.setMsg("Error: Could not add funds!");
		}

		return response;
		
	}

	@Override
	@Transactional
	public Response<String> withdrawAmount(String accNumber, double amount) {

		Response<String> response = new Response<String>();
		double currentBalance = 0.0;
		double newBalance = 0.0;

		try {
			currentBalance = repository.getAccount(accNumber).getBalance();
			if (amount > currentBalance) throw new IncompleteFundsException(INADEQUATE_FUNDS_ERROR_MSG);
			newBalance = currentBalance - amount;
			repository.updateBalance(newBalance, accNumber);
			response.setStatus(true);
			response.setMsg(NEW_BALANCE + newBalance);
		} catch (IncompleteFundsException ie) {
			logger.error(TRANSFER_FUNDS_FAILED_WITH_ERROR + ie.getMessage());
			response.setStatus(false);
			response.setMsg(ie.getMessage());
		} catch (Exception ex) {
			logger.error(WITHDRAW_FUNDS_FAILED_WITH_ERROR + ex.getMessage() + ex.getCause());
			response.setStatus(false);
			response.setMsg("Error: Check account exists and necessary funds available");
		}

		return response;
		
	}

	@Override
	@Transactional
	public Response<String> transferFunds(double amount, String fromAccountNo, String toAccountNo) {

		Response<String> response = new Response<String>();
		double fromBalance = 0.0;
		double toBalance = 0.0;
		try {
			fromBalance = repository.getAccount(fromAccountNo).getBalance();
			toBalance = repository.getAccount(toAccountNo).getBalance();
			if (amount > fromBalance) throw new IncompleteFundsException(INADEQUATE_FUNDS_ERROR_MSG);
			repository.updateBalance(fromBalance - amount, fromAccountNo);
			repository.updateBalance(toBalance + amount, toAccountNo);
			response.setStatus(true);
			response.setMsg("Transfer complete");
		} catch (IncompleteFundsException ie) {
			logger.error(TRANSFER_FUNDS_FAILED_WITH_ERROR + ie.getMessage());
			response.setStatus(false);
			response.setMsg(ie.getMessage());
		} catch (Exception ex) {
			logger.error(TRANSFER_FUNDS_FAILED_WITH_ERROR + ex.getMessage() + ex.getCause());
			response.setStatus(false);
			response.setMsg("Transfer could not be completed");
		}

		return response;
		
	}

	@Override
	public Response<String> addAccount(Person person) {

		Response<String> response = new Response<String>();
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setFirstName(person.getFirstName());
		accountEntity.setLastName(person.getLastName());
		accountEntity.setBalance(0);
		accountEntity.setEmail(person.getEmail());
		accountEntity.setAccNumber(person.getAccNumber());
		try {
			repository.addAccount(accountEntity);
			response.setStatus(true);
			response.setMsg(person.toString());
		} catch (Exception ex) {
			response.setStatus(false);
			response.setMsg("Error: Could not add account.");
		}
		return response;
		
	}

	@Override
	public Response<List<Person>> getAccounts() {
		
		Response<List<Person>> response = new Response<List<Person>>();
		List<Person> persons = new ArrayList<Person>();
		try {
		List<AccountEntity> accounts = repository.getListOfAccounts();
		persons = accounts.stream().map(p -> mapToPerson(p)).collect(Collectors.toList());
		response.setStatus(true);
		response.setMsg(persons);
		} catch (Exception ex) {
			response.setStatus(false);
			response.setMsg(persons);
		}
		return response;
		
	}

	private Person mapToPerson(AccountEntity account) {
		Person person = new Person();
		person.setFirstName(account.getFirstName());
		person.setLastName(account.getLastName());
		person.setEmail(account.getEmail());
		person.setAccNumber(account.getAccNumber());
		return person;
	}

}
