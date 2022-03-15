package com.cadman.app.banking.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.cadman.app.banking.entity.AccountEntity;
import com.cadman.app.banking.repository.BankingRepository;
import com.cadman.app.banking.response.Response;

/**
 * 
 * @author Phil Cadwallader
 * 
 * Small Sample Tests to demonstrate JUnit compilation
 * 
 * Current stage is just for Junit and mocking example, not all tests added
 * 
 * TODO: All JUnit tests for all methods with coverage testing checked
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class BankingServiceImplTest {

	private static final String DUMMY_LAST_NAME = "Snow";
	private static final String DUMMY_FIRST_NAME = "Jon";
	private static final String DUMMY_EMAIL = "jon.snow@got.com";
	private static final String DUMMY_ACC_NUMBER = "1234";

	@InjectMocks
	BankingServiceImpl service;

	@Mock
	BankingRepository repository;

	@Test
	public void getBalanceReturnsValue() {
		mockAccount();
		Response<String> response = service.getBalance(DUMMY_ACC_NUMBER);
		assertEquals("Your remaining balance: €100.0", response.getMsg());
		assertTrue(response.isStatus());
	}

	@Test
	public void getBalanceReturnsError() {
		mockAccountError();
		Response<String> response = service.getBalance(DUMMY_ACC_NUMBER);
		assertEquals("Error: Could not retrieve balance, check account exists.", response.getMsg());
		assertFalse(response.isStatus());
	}

	private void mockAccount() {
		AccountEntity account = new AccountEntity().builder().firstName(DUMMY_FIRST_NAME).lastName(DUMMY_LAST_NAME)
				.accNumber(DUMMY_ACC_NUMBER).balance(100.00).email(DUMMY_EMAIL).build();
		when(repository.getAccount(any(String.class))).thenReturn(account);
	}

	private void mockAccountError() {
		when(repository.getAccount(any(String.class))).thenThrow(new RuntimeException());
	}

}
