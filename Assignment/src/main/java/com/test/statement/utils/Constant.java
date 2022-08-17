package com.test.statement.utils;

import java.util.regex.Pattern;

public class Constant {

    public static final String NO_ACCOUNT_FOUND =
            "Unable to find an account matching this account ID";
    public static final String INVALID_ACCOUNT_ID =
            "The provided account ID did not match the expected format";
    public static final String INVALID_FROM_TO_AMOUNTS =
            "The provided fromAmount or toAmount is not valid.";
    public static final String INVALID_FROM_TO_DATES =
            "The provided date ranges is not valid.";

    public static final Pattern ACCOUNT_ID_PATTERN = Pattern.compile("^[1-9]$");

}
