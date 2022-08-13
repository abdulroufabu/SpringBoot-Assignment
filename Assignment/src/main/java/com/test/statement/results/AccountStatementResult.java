package com.test.statement.results;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class AccountStatementResult {

	@JsonInclude(Include.NON_NULL)
	private String errorMessage;
	
	@JsonInclude(Include.NON_NULL)
	private String accountNumber;
	
	@JsonInclude(Include.NON_NULL)
	private List<AccountStatement> statements;
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public List<AccountStatement> getStatements() {
		return statements;
	}
	public void setStatements(List<AccountStatement> statement) {
		this.statements = statement;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	@Override
	public String toString() {
		return "AccountDetails [errorMessage=" + errorMessage + ", accountNumber=" + accountNumber + ", statement="
				+ statements + "]";
	}
	
	

}
