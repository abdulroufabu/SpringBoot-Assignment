package com.test.statement.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "account_type")
	private String accountType;

	@Column(name = "account_number")
	private String accountNumber;

	public Account() {
		
	}

	public Account(String accountType, String accountNumber) {
		this.accountType = accountType;
		this.accountNumber = accountNumber;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", accountType=" + accountType + ", accountNumber=" + accountNumber + "]";
	}
	
	
}