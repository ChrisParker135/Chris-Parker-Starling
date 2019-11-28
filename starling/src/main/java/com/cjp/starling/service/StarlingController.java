package com.cjp.starling.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cjp.starling.model.Accounts;
import com.cjp.starling.util.RoundupCalculator;

/**
 * Control the flow of requests and responses
 * 
 * @author Chris Parker
 *
 */
public class StarlingController {

	private final static String ACCOUNTS = "accounts";
	private final static String ACCOUNT_UID = "accountUid";
	private final static String DEFAULT_CATEGORY = "defaultCategory";
	private final static String FEEDITEMS = "feedItems";
	private static final String STARLING_API_URL = "https://api-sandbox.starlingbank.com/api/v2/";

	/**
	 * Utility to calculate roundUp totals
	 */
	private RoundupCalculator calculator = new RoundupCalculator();;
	/**
	 * Service providing access to the Starling API
	 */
	private StarlingAPIService stirlingApiService;
	/**
	 * Start date time for the week
	 */
	private String fromDate = "";
	/**
	 * End date time for the week
	 */
	private String toDate = "";

	/**
	 * Execute the different processes to get the required feedUnit values, round up
	 * and assign to a new savings goal
	 * 
	 * @param accessToken
	 * @param weekBeginning
	 * @param savingsGoal
	 */
	public void run(String accessToken, String weekBeginning, String savingsGoal, String transferUID) {

		derivedStartandEndTimes(weekBeginning);

		Accounts accounts = getAccounts(accessToken);

		Collection<String> minorUnits = getFeedUnits(accounts, accessToken);

		String roundUpTotal = calculator.caluculationRoundupTotal(minorUnits);

		String savingsGoalUID = createSavingsGoal(savingsGoal, accounts, accessToken);

		Boolean success = addMoneyToSavingsGoal(savingsGoalUID, savingsGoal, accounts, roundUpTotal, transferUID, accessToken);
		if (success) {
			System.out.println("Upload of roundup minor units " + roundUpTotal + " to savings goal " + savingsGoal
					+ " was a success");
		} else {
			System.out.println("Upload of roundup minor units " + roundUpTotal + " to savings goal " + savingsGoal
					+ " was not a success");
		}

	}

	/**
	 * Get Accounts accountUID and defaultCategory
	 * @return Accounts accounts 
	 */
	private Accounts getAccounts(String accessToken) {
		Accounts accounts = new Accounts();
		// Get AccountUID and Default Category
		String getBankAccountsUrl = STARLING_API_URL + "accounts";
		JSONObject jsonObject = stirlingApiService.getRequest(getBankAccountsUrl, accessToken);
		if (jsonObject != null) {
			JSONArray jsonAccounts = jsonObject.getJSONArray(ACCOUNTS);
			JSONObject jsonAccountsOject = (JSONObject) jsonAccounts.get(0);
			accounts.setAccountUID((String) jsonAccountsOject.get(ACCOUNT_UID));
			accounts.setDefaultCategory(jsonAccountsOject.getString(DEFAULT_CATEGORY));
		}
		return accounts;
	}

	/**
	 * Get the account holder's feed items minorUnits which were created between two timestamps
	 * @param accounts Accounts
	 * @param accessToken String
	 * @return Collection<String> list of minorUnits
	 */
	private Collection<String> getFeedUnits(Accounts accounts, String accessToken) {

		Collection<String> minorUnits = new ArrayList<String>();
		String getTransactionFeedsUrl = STARLING_API_URL + "feed/account/" + accounts.getAccountUID() + "/category/"
				+ accounts.getDefaultCategory() + "/transactions-between?minTransactionTimestamp=" + fromDate
				+ "&maxTransactionTimestamp=" + toDate;
		System.out.println("gettransactionfeedurl " + getTransactionFeedsUrl);
		JSONObject jsonFeedUnits = stirlingApiService.getRequest(getTransactionFeedsUrl, accessToken);
		if (jsonFeedUnits != null) {
			JSONArray jsonFeedItems = jsonFeedUnits.getJSONArray(FEEDITEMS);
			jsonFeedItems.forEach(feedItem -> {
				JSONObject obj = (JSONObject) feedItem;
				JSONObject amount = (JSONObject) obj.get("amount");
				long minorUnit = amount.getLong("minorUnits");
				System.out.println("minorunit: " + minorUnit);
				minorUnits.add(String.valueOf(minorUnit));
			});
		}
		return minorUnits;
	}

	/**
	 * Create a new savings goal
	 * @param savings goal name String
	 * @param accounts Accounts 
	 * @param accessToken String 
	 * @return String savings goal UID
	 */
	private String createSavingsGoal(String savingsGoal, Accounts accounts, String accessToken) {

		String savingsGoalUID = "";
		String createSavingsGoal = STARLING_API_URL + "account/" + accounts.getAccountUID() + "/savings-goals/";
		String createGoalBody = "{\"name\": \"" + savingsGoal
				+ "\",\"currency\": \"GBP\",\"target\": {\"currency\": \"GBP\",\"minorUnits\": 123456},\"base64EncodedPhoto\": \"string\"}";
		JSONObject createGoalResponse = stirlingApiService.putRequest(createSavingsGoal, accessToken, createGoalBody);
		if (createGoalResponse != null) {
			savingsGoalUID = (String) createGoalResponse.get("savingsGoalUid");
			System.out.println("Savings goal named " + savingsGoal + " created");
		}

		return savingsGoalUID;
	}

	/**
	 * Add money to a savings goal
	 * @param savingsGoalUID String
	 * @param savings name String 
	 * @param accounts Accounts
	 * @param roundUpTotal String
	 * @param transferUID String
	 * @param accessToken String
	 * @return Boolean success if true, failure otherwise
	 */
	private Boolean addMoneyToSavingsGoal(String savingsGoalUID, String savingsGoal, Accounts accounts, String roundUpTotal, String transferUID, String accessToken) {
		Boolean success = false;
		String addMoneyToSavingsGoal = STARLING_API_URL + "account/" + accounts.getAccountUID() + "/savings-goals/"
				+ savingsGoalUID + "/add-money/" + transferUID;
		String body = "{\"amount\": {\"currency\": \"GBP\", \"minorUnits\": " + roundUpTotal + "}}";

		JSONObject addMoneyResponse = stirlingApiService.putRequest(addMoneyToSavingsGoal, accessToken, body);
		if (addMoneyResponse != null) {
			success = (Boolean) addMoneyResponse.get("success");
		}
		return success;
		
	}

	/**
	 * Determine the start date time and end date time given a week beginning day,
	 * ensuring the dates are yyyy-MM-dd'T'HH:mm:ss.SSSZ format
	 * 
	 * @param weekBeginning String week beginning date in format yyyy-MM-dd
	 */
	private void derivedStartandEndTimes(String weekBeginning) {

		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

		try {
			Date dateWeekBeginning = simpleDateFormat1.parse(weekBeginning);
			fromDate = simpleDateFormat2.format(simpleDateFormat1.parse(weekBeginning)).replace(".000+0000", ".000Z");
			LocalDateTime localDateTime = LocalDateTime.ofInstant(dateWeekBeginning.toInstant(), ZoneId.systemDefault())
					.plusDays(8).minusNanos(1);
			Date out = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
			toDate = simpleDateFormat2.format(out).replace(".999+0000", ".999Z");

		} catch (ParseException e) {
			System.out.println("Error parsing date " + weekBeginning + " " + e.getMessage());
		}

	}

	/**
	 * Setter for StirlingApiService
	 * @param startlingApiService StirlingApiService
	 */
	public void setStarlingAPIService(StarlingAPIService startlingApiService) {
		this.stirlingApiService = startlingApiService;
	}
}
