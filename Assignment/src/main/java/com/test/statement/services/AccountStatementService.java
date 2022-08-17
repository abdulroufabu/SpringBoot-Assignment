package com.test.statement.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.statement.exceptions.InvalidParameterException;
import com.test.statement.exceptions.ResourceNotFoundException;
import com.test.statement.models.Account;
import com.test.statement.models.Statement;
import com.test.statement.repositories.AccountRepository;
import com.test.statement.repositories.StatementRepository;
import com.test.statement.results.AccountStatement;
import com.test.statement.results.AccountStatementResult;
import com.test.statement.utils.Constant;

@Service
public class AccountStatementService {

	private final AccountRepository accountRepository;
	private final StatementRepository statementRepository;

	@Autowired
	public AccountStatementService(AccountRepository accountRepository, StatementRepository statementRepository) {
		this.accountRepository = accountRepository;
		this.statementRepository = statementRepository;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountStatementService.class);

	public AccountStatementResult getAccountDetails(Long accountId, LocalDate fromDate, LocalDate toDate,
			Double fromAmount, Double toAmount) throws InvalidParameterException, ResourceNotFoundException {

		AccountStatementResult statmentResult = new AccountStatementResult();

		if (toDate == null && fromDate == null) {
			// If the request does not specify any search parameter, then the search will return three months back statement
			fromDate = LocalDate.now().minusMonths(3);
			toDate = LocalDate.now();
		}

		Account accountData = accountRepository.findById(Long.valueOf(accountId))
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NO_ACCOUNT_FOUND));

		String hashedAccountNumber = "XXXXXXXXXX" + accountData.getAccountNumber().substring(9, 13);
		statmentResult.setAccountNumber(hashedAccountNumber);

		List<Statement> stmnts = statementRepository.findByAccountId(Long.valueOf(accountId));

		if (null != stmnts && !stmnts.isEmpty()) {
			LOGGER.info("Processing account statements fetched from DB");

			List<AccountStatement> statementList = getFilteredStatementList(stmnts, 
					fromDate, toDate, 
					fromAmount,	toAmount);
			statmentResult.setStatements(statementList);
		}

		return statmentResult;
	}

	
	private List<AccountStatement> getFilteredStatementList(List<Statement> stmnts, LocalDate fDate,
			LocalDate tDate, Double fromAmount, Double toAmount) {

		List<AccountStatement> statementList = new ArrayList<AccountStatement>();

		Predicate<Statement> isBetweenFromAndToDates = e -> {
			try {
				LocalDate accountDate = convertDBStringToDate(e.getDatefield());
				return (accountDate.isAfter(fDate) && accountDate.isBefore(tDate));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return false;
		};

		if (toAmount != null && fromAmount != null) {

			Predicate<Statement> isBetweenFromAndToAmounts = e -> {
				try {
					Double dbAmount = Double.parseDouble(e.getAmount());
					return ((dbAmount >= fromAmount) && (dbAmount <= toAmount));
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				return false;
			};

			statementList = stmnts.stream()
					.filter(isBetweenFromAndToDates.and(isBetweenFromAndToAmounts))
					.map(stmnt -> new AccountStatement(stmnt.getDatefield(), stmnt.getAmount()))
					.collect(Collectors.toList());

		} else {

			statementList = stmnts.stream()
					.filter(isBetweenFromAndToDates)
					.map(stmnt -> new AccountStatement(stmnt.getDatefield(), stmnt.getAmount()))
					.collect(Collectors.toList());
		}
		return statementList;

	}

	private static LocalDate convertDBStringToDate(String date) throws Exception {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate convertedDate = LocalDate.parse(date.replace(".", "-"), formatter);
		return convertedDate;
	}

}
