package com.cadman.app.banking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cadman.app.banking.model.Person;
import com.cadman.app.banking.response.Response;
import com.cadman.app.banking.service.BankingServiceImpl;

@RestController
@RequestMapping(value = "/accounts")
public class BankingController {

	@Autowired
	BankingServiceImpl service;

	@GetMapping(value = "/all", produces = "application/json")
	public Response<List<Person>> listAccounts() {

		Response<List<Person>> response = service.getAccounts();
		return response;
	}

	@GetMapping(value = "/balance/{accountNumber}", produces = "application/json")
	public Response<String> displayBalance(@PathVariable(value = "accountNumber") String accNumber) {

		Response<String> response = service.getBalance(accNumber);
		return response;
	}

	@PostMapping(value = "/add")
	public Response<String> addAccount(@RequestBody Person person) {

		Response<String> response = service.addAccount(person);
		return response;
	}

	@PatchMapping(value = "/funds/add", produces = "application/json")
	public Response<String> increaseBalance(@RequestParam String accountNumber, @RequestParam double amount) {
		Response<String> response = service.addAmount(amount, accountNumber);
		return response;
	}

	@PatchMapping(value = "/funds/withdraw", produces = "application/json")
	public Response<String> withdrawFunds(@RequestParam String accountNumber, @RequestParam double amount) {

		Response<String> response = service.withdrawAmount(accountNumber, amount);
		return response;
	}

	@PatchMapping(value = "/funds/transfer", produces = "application/json")
	public Response<String> transferFunds(@RequestParam(value = "fromAccount") String fromAccountNumber,
			@RequestParam(value = "toAccount") String toAccountNumber, @RequestParam double amount) {

		Response<String> response = service.transferFunds(amount, fromAccountNumber, toAccountNumber);
		return response;
	}

}
