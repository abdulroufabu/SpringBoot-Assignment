package com.test.statement.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
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
import com.test.statement.utils.InputValidator;

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

	public AccountStatementResult getAccountDetails(String accountId, String fromDate, String toDate, String fromAmount,
			String toAmount) throws InvalidParameterException, ResourceNotFoundException {

		if (!InputValidator.isAccountIdValid(accountId)) {
			throw new InvalidParameterException(Constant.INVALID_ACCOUNT_ID);
		}
		
		AccountStatementResult statmentResult = new AccountStatementResult();
		
		if (StringUtils.isBlank(toDate) && StringUtils.isBlank(fromDate)) {
			//If the request does not specify any search parameter, then the search will return three months back statement
			fromDate = LocalDate.now().minusMonths(3).toString();
			toDate = LocalDate.now().toString();
		}
		
		Optional<Account> accountDataOpt = accountRepository.findById(Long.valueOf(accountId));
		if (accountDataOpt.isPresent()) {
			
			Account accountData = accountDataOpt.get();
			
			String hashedAccountNumber = "XXXXXXXXXX" + accountData.getAccountNumber().substring(9, 13);
			statmentResult.setAccountNumber(hashedAccountNumber);

			LocalDate fromDt = LocalDate.parse(fromDate);
			LocalDate toDt = LocalDate.parse(toDate);
			
			if (!InputValidator.isValidDateRange(fromDt, toDt)) {
				throw new InvalidParameterException(Constant.INVALID_FROM_TO_DATES);
			}

			List<Statement> stmnts = statementRepository.findByAccountId(Long.valueOf(accountId));
			
			if(null != stmnts && !stmnts.isEmpty()) {
				
				LOGGER.info("Processing account statements fetched from DB");
				
				Predicate<Statement> isBetweenFromAndToDates = e -> {
					try {
						LocalDate accountDate = convertDBStringToDate(e.getDatefield());
						return (accountDate.isAfter(fromDt) && accountDate.isBefore(toDt));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					return false;
				};
				
				List<AccountStatement> statementList = new ArrayList<AccountStatement>();
				
				if (StringUtils.isNotBlank(toAmount) && StringUtils.isNotBlank(fromAmount)) {
					
					final double toAmt = Double.parseDouble(toAmount);
					final double fromAmt = Double.parseDouble(fromAmount);

					if (!InputValidator.isValidAmountRange(fromAmt, toAmt)) {
						throw new InvalidParameterException(Constant.INVALID_FROM_TO_AMOUNTS);
					}
					
					Predicate<Statement> isBetweenFromAndToAmounts = e -> {
						try {
							Double dbAmount = Double.parseDouble(e.getAmount());
							return ((dbAmount >= fromAmt) && (dbAmount <= toAmt));
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
				
				statmentResult.setStatements(statementList);
			}
			
		} else {
			LOGGER.error(Constant.NO_ACCOUNT_FOUND);
			throw new ResourceNotFoundException(Constant.NO_ACCOUNT_FOUND);
		}

		return statmentResult;
	}

	private static LocalDate convertDBStringToDate(String date) throws Exception {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate convertedDate = LocalDate.parse(date.replace(".", "-"), formatter);
		return convertedDate;
	}

}
