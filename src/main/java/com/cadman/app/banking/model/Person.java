package com.cadman.app.banking.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class Person {

	private String firstName;
	private String lastName;
	private String accNumber;
	private String email;
	
	@Override
	public String toString() {
		return "Account Number = " + accNumber + ", Name = " + firstName + " " + lastName + ", Email: " + email;
	}
	
	
	
	
}
