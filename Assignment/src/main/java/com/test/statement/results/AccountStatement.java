package com.test.statement.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class AccountStatement {

	@JsonInclude(Include.NON_NULL)
	private String accountNumber;
	private String date;
	private String amount;
	
	public AccountStatement() {
	}
	
	public AccountStatement(String accountNumber, String date, String amount) {
		this.accountNumber = accountNumber;
		this.date = date;
		this.amount = amount;
	}
	
	public AccountStatement(String date, String amount) {
		this.date = date;
		this.amount = amount;
	}

	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount	 = amount;
	}
}
