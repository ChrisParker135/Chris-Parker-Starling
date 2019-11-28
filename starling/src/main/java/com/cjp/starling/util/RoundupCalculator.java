package com.cjp.starling.util;

import java.util.Collection;

/**
 * Calculator for Roundup
 * 
 * @author Chris Parker
 *
 */
public class RoundupCalculator {

	private int roundUpTotals;
	
	/**
	 * For a collection of feedUnit pence values calculate the total of remainders from
	 * rounded up pound values.
	 * 
	 * @param feedUnits Collection of String values
	 * @return String total of round ups
	 */
	public String caluculationRoundupTotal(Collection<String> feedUnits) {
		
		roundUpTotals = 0;
		
		feedUnits.forEach(feedUnit -> {
			int roundUpValue = calculateRoundUp(feedUnit);
			roundUpTotals = roundUpTotals + roundUpValue;
		});

		return String.valueOf(roundUpTotals);
	}

	/**
	 * Take a String integer, convert to a Double, calculate the difference between
	 * the value rounded up and the value, multiply by 100 and convert to an integer
	 * 
	 * @param feedUnit String value in pence
	 * @return String round up value
	 */
	private int calculateRoundUp(String feedUnit) {
		Double feedDouble = Double.parseDouble(feedUnit);
		double pounds = feedDouble / 100;
		double fraction = (Math.ceil(pounds) - pounds) * 100;
		return (int)Math.round(fraction);
	}

}
