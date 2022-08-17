package com.test.statement.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.statement.exceptions.InvalidParameterException;
import com.test.statement.exceptions.UnauthorizedException;

/**
 * @author abdul.abu
 *
 */
public class AuthorizationUtil {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationUtil.class);

	public enum Role {
		ADMIN, USER;
	}

	public enum Feature {
		NONE, SEARCH_BY_DATE, SEARCH_BY_AMOUNT, SEARCH_BY_ACCOUNT_ID;
	}

	private static Map<Role, List<Feature>> rolefeatures;
	static {
		mapFeaturesByRole();
	}

	private static void mapFeaturesByRole() {

		rolefeatures = new HashMap<Role, List<Feature>>();

		List<Feature> adminFeatures = new ArrayList<Feature>();
		adminFeatures.add(Feature.SEARCH_BY_ACCOUNT_ID);
		adminFeatures.add(Feature.SEARCH_BY_AMOUNT);
		adminFeatures.add(Feature.SEARCH_BY_DATE);
		rolefeatures.put(Role.ADMIN, adminFeatures);

		List<Feature> userFeatures = new ArrayList<Feature>();
		userFeatures.add(Feature.SEARCH_BY_ACCOUNT_ID);
		rolefeatures.put(Role.USER, userFeatures);
	}

	public static HashSet<Feature> getFeatures(List<Role> roles) {

		HashSet<Feature> features = new HashSet<Feature>();

		for (Role role : roles) {
			if (!rolefeatures.containsKey(role)) {
				continue;
			}
			features.addAll(rolefeatures.get(role));
		}
		return features;
	}

	public static void validateInputRequest(SearchInput parameters, List<Role> userRoles)
			throws InvalidParameterException, UnauthorizedException {

		StringBuilder errorMessage = new StringBuilder("");

		HashSet<Feature> features = getFeatures(userRoles);

		validateAccountIdFilter(parameters, features, errorMessage);

		validateAmountFilter(parameters, features, errorMessage);

		validateDateFilter(parameters, features, errorMessage);

		if (!errorMessage.toString().isEmpty()) {
			logger.error("Invalid input :" + errorMessage.toString());
			throw new InvalidParameterException(errorMessage.toString());
		}
	}

	private static StringBuilder validateAccountIdFilter(SearchInput parameters, HashSet<Feature> features,
			StringBuilder errorMessage) throws UnauthorizedException {

		Long accountId = parameters.getAccountId();

		if (!features.contains(Feature.SEARCH_BY_ACCOUNT_ID) && accountId != 0) {
			String msg = "User does not have the access to filter by account ID!";
			logger.error("Unauthorized Access :" + errorMessage.toString());
			throw new UnauthorizedException(msg);
		}

		if (!InputValidator.isAccountIdValid(String.valueOf(accountId))) {
			errorMessage.append(Constant.INVALID_ACCOUNT_ID + ", ");

		}
		return errorMessage;
	}

	private static StringBuilder validateDateFilter(SearchInput parameters, HashSet<Feature> features,
			StringBuilder errorMessage) throws UnauthorizedException {

		LocalDate fromDate = parameters.getFromDate();
		LocalDate toDate = parameters.getToDate();

		if (!features.contains(Feature.SEARCH_BY_DATE) && (fromDate != null || toDate != null)) {
			String msg = "User does not have the access to filter by dates";
			logger.error("Unauthorized Access :" + errorMessage.toString());
			throw new UnauthorizedException(msg);
		}
		if (!InputValidator.isValidDateRange(fromDate, toDate)) {
			errorMessage.append(Constant.INVALID_FROM_TO_DATES + ", ");
		}
		return errorMessage;
	}

	private static StringBuilder validateAmountFilter(SearchInput parameters, HashSet<Feature> features,
			StringBuilder errorMessage) throws UnauthorizedException {

		Double fromAmount = parameters.getFromAmount();
		Double toAmount = parameters.getToAmount();

		if (!features.contains(Feature.SEARCH_BY_AMOUNT) && (fromAmount != null || toAmount != null)) {
			String msg = "User does not have the access to filter by amounts.";
			logger.error("Unauthorized Access :" + errorMessage.toString());
			throw new UnauthorizedException(msg);
		}

		if (!InputValidator.isValidAmountRange(fromAmount, toAmount)) {
			errorMessage.append(Constant.INVALID_FROM_TO_AMOUNTS);
		}
		return errorMessage;
	}

}
