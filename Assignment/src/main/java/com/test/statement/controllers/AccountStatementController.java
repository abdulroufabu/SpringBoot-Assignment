package com.test.statement.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.statement.exceptions.InvalidParameterException;
import com.test.statement.exceptions.ResourceNotFoundException;
import com.test.statement.results.AccountStatementResult;
import com.test.statement.services.AccountStatementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Account Statement Services",
description = "This endpoints provides the capability to view statements by performing simple search on date and amount ranges")
public class AccountStatementController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountStatementController.class);

	private final AccountStatementService accountService;

	@Autowired
	public AccountStatementController(AccountStatementService accountService) {
		this.accountService = accountService;
	}

	@GetMapping(value = "/account")
	@Operation(summary = "Account Statment search operation for user with ADMIN role only.", responses = {
			@ApiResponse(description = "Account Statment search operation using account id and specify the date range or amount range. "
					+ "If the request does not specify any parameter, then the search will return three months back statement.", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountStatementResult.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content) })
	public ResponseEntity<?> searchAccountStatement(@RequestParam(name = "id", required = false) String accountID,
			@RequestParam(name = "fromDate", required = false) String fromDate,
			@RequestParam(name = "toDate", required = false) String toDate,
			@RequestParam(name = "fromAmount", required = false) String fromAmount,
			@RequestParam(name = "toAmount", required = false) String toAmount) {

		LOGGER.debug("Invoked AccountStatementController.searchAccountStatement with Admin role");

		AccountStatementResult statementResult = new AccountStatementResult();

		// Attempt to retrieve the statement information
		try {
			statementResult = accountService.getAccountDetails(accountID, fromDate, toDate, fromAmount, toAmount);

			// Return the account details:
			return new ResponseEntity<>(statementResult, HttpStatus.OK);

		} catch (ResourceNotFoundException e1) {

			statementResult.setErrorMessage(e1.getMessage());
			return new ResponseEntity<>(statementResult, HttpStatus.NOT_FOUND);
		} catch (InvalidParameterException | NumberFormatException e2) {

			statementResult.setErrorMessage(e2.getMessage());
			return new ResponseEntity<>(statementResult, HttpStatus.BAD_REQUEST);
		} catch (Exception error) {

			statementResult.setErrorMessage(error.getMessage());
			return new ResponseEntity<>(statementResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/account/{id}")
	@Operation(summary = "Account Statment search operation for user with ADMIN or USER role.", responses = {
			@ApiResponse(description = "Account Statment search operation using acount id only for three months back from now.", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountStatementResult.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content) })
	public ResponseEntity<?> searchAccountStatement(@PathVariable("id") String accountId) {

		LOGGER.debug("Invoked AccountStatementController.searchAccountStatement with user role.");

		AccountStatementResult statementResult = new AccountStatementResult();

		// Attempt to retrieve the statement information
		try {
			statementResult = accountService.getAccountDetails(accountId, null, null, null, null);

			// Return the account details:
			return new ResponseEntity<>(statementResult, HttpStatus.OK);

		} catch (ResourceNotFoundException e1) {

			LOGGER.error("Error:" + e1.getMessage());
			statementResult.setErrorMessage(e1.getMessage());
			return new ResponseEntity<>(statementResult, HttpStatus.NOT_FOUND);
		} catch (InvalidParameterException | NumberFormatException e2) {

			LOGGER.error("Error:" + e2.getMessage());
			statementResult.setErrorMessage(e2.getMessage());
			return new ResponseEntity<>(statementResult, HttpStatus.BAD_REQUEST);
		} catch (Exception error) {

			LOGGER.error("Internal Server Error:" + error.getMessage());
			statementResult.setErrorMessage(error.getMessage());
			return new ResponseEntity<>(statementResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
