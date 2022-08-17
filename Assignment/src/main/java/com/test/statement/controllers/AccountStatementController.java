package com.test.statement.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.statement.results.AccountStatementResult;
import com.test.statement.services.AccountStatementService;
import com.test.statement.utils.SearchInput;

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
	
	@PostMapping(value = "/accounts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Account Statment search operation for user with ADMIN or USER role.", responses = {
			@ApiResponse(description = "Account Statment search operation.", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountStatementResult.class))),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content) })
    public ResponseEntity<?> checkAccountStatement(
			@Valid @RequestBody SearchInput searchInput) {

		LOGGER.debug("Invoked AccountStatementController.checkAccountStatement");

		AccountStatementResult statementResult = accountService.getAccountDetails(searchInput.getAccountId(),
				searchInput.getFromDate(), searchInput.getToDate(), searchInput.getFromAmount(),
				searchInput.getToAmount());
		// Return the account details:
		return new ResponseEntity<>(statementResult, HttpStatus.OK);
	}

}
