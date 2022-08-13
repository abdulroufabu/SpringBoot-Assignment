package com.test.statement.utils;

import java.time.LocalDate;

public class InputValidator {


    public static boolean isAccountIdValid(String accountId) {
    	return Constant.ACCOUNT_ID_PATTERN.matcher(accountId).find();
    }

    public static boolean isValidAmountRange(double fromAmt, double toAmt) {
    	
    	if (fromAmt < 0 || toAmt <= 0) {
    		return false;
    	}
    	
    	if (fromAmt > toAmt) {
    		return false;
    	}
		return true;
    }
    
    public static boolean isValidDateRange(LocalDate fromDt, LocalDate toDt) {
    	
    	if (fromDt.isAfter(toDt)) {
    		return false;
    	}
		return true;
    }
}
