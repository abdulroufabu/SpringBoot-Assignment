package com.test.statement.utils;

import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SearchInput {

	@Positive(message = "Account ID must be positive!")
    @Min(value = 1, message = "Account ID must be larger than 0!")
	@NotBlank(message = "Account ID is mandatory!")
	private Long accountId;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate fromDate;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate toDate;
	
	@Positive(message = "From Amount must be positive!")
	private Double fromAmount;
	
	@Positive(message = "To Amount must be positive!")
	private Double toAmount;

    public SearchInput() {
    }

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public Double getFromAmount() {
		return fromAmount;
	}

	public void setFromAmount(Double fromAmount) {
		this.fromAmount = fromAmount;
	}

	public Double getToAmount() {
		return toAmount;
	}

	public void setToAmount(Double toAmount) {
		this.toAmount = toAmount;
	}

	@Override
	public String toString() {
		return "SearchInput [accountId=" + accountId + ", fromDate=" + fromDate + ", toDate=" + toDate + ", fromAmount="
				+ fromAmount + ", toAmount=" + toAmount + "]";
	}

    
}