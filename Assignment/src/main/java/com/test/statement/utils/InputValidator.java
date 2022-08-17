package com.test.statement.utils;

import java.time.LocalDate;


/**
 * @author Abdulrouf
 *
 */
public class InputValidator {

	public static boolean isAccountIdValid(String accountId) {
		return Constant.ACCOUNT_ID_PATTERN.matcher(accountId).find();
	}

	public static boolean isValidAmountRange(Double fromAmt, Double toAmt) {

		if (fromAmt != null && toAmt != null && (fromAmt > toAmt)) {
			return false;
		}

		if ((fromAmt != null && toAmt == null) || (fromAmt == null && toAmt != null)) {
			return false;
		}
		return true;
	}

	public static boolean isValidDateRange(LocalDate fromDt, LocalDate toDt) {

		if (fromDt != null && toDt != null && fromDt.isAfter(toDt)) {
			return false;
		}
		return true;
	}
}
