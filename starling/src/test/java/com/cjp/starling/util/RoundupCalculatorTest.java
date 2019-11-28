package com.cjp.starling.util;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;
/**
 * Unit test for RoundupCalculator
 * 
 * @author Chris Parker
 *
 */
public class RoundupCalculatorTest extends TestCase {

	private RoundupCalculator calculator;
	

	public void setUp() throws Exception {
		calculator = new RoundupCalculator();
	}


	public void testCaluculationRoundupTotalSingle() {
		try {
			
			Collection<String> feedUnits = new ArrayList<String>();
			feedUnits.add("1234");
			
			String total = calculator.caluculationRoundupTotal(feedUnits);
			
			assertEquals("Total not as expected", "66", total);
			
		} catch (Exception e) {
			fail("testCaluculationRoundupTotalSingle failed. " + e.getMessage());
		}	
	}
	

	public void testCaluculationRoundupTotalMultipleMax() {
		try {
			
			Collection<String> feedUnits = new ArrayList<String>();
			feedUnits.add("1234");
			feedUnits.add("1299");
			
			String total = calculator.caluculationRoundupTotal(feedUnits);
			
			assertEquals("Total not as expected", "67", total);
			
		} catch (Exception e) {
			fail("testCaluculationRoundupTotalMultipleMax failed. " + e.getMessage());
		}
		
	}

	
	public void testCaluculationRoundupTotalMultipleMin() {
		try {
			
			Collection<String> feedUnits = new ArrayList<String>();
			feedUnits.add("1234");
			feedUnits.add("1201");
			
			String total = calculator.caluculationRoundupTotal(feedUnits);
			
			assertEquals("Total not as expected", "165", total);
			
		} catch (Exception e) {
			fail("testCaluculationRoundupTotalMultipleMin failed. " + e.getMessage());
		}
		
	}
	
	
	public void testCaluculationRoundupTotalWholePound() {
		try {
			
			Collection<String> feedUnits = new ArrayList<String>();
			feedUnits.add("1234");
			feedUnits.add("1300");
			
			String total = calculator.caluculationRoundupTotal(feedUnits);
			
			assertEquals("Total not as expected", "66", total);
			
		} catch (Exception e) {
			fail("testCaluculationRoundupTotalWholePound failed. " + e.getMessage());
		}
		
	}

}
