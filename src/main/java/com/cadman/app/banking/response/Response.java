package com.cadman.app.banking.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<T> {

	private boolean status;
	private T msg;
	
}
