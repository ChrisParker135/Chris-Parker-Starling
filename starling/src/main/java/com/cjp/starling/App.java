package com.cjp.starling;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import com.cjp.starling.service.StarlingAPIService;
import com.cjp.starling.service.StarlingController;

/**
 * Starling Technical Challenge - App with a main method
 *
 */
public class App 
{
	private final static String ACCESS_TOKEN = "authorization";
	private final static String BEARER = "Bearer ";
	private final static String PROPERTIES_FILE = "c:/properties/properties.txt";
	
	/**
	 * Use the Starling API to get all the transaction feeds for a given week, determine round up totals and add to a new saving goal
	 * @param args String[] the first argument is the week beginning date, the second is the name of saving goal
	 */
    public static void main( String[] args )
    {
    	Properties properties = new Properties();
		try {
			FileInputStream inputStream = new FileInputStream(PROPERTIES_FILE);
			properties.load(inputStream);
			inputStream.close();
			String accessToken = BEARER + properties.getProperty(ACCESS_TOKEN);
			
			StarlingAPIService starlingApiService = new StarlingAPIService();
			
			StarlingController starlingController = new StarlingController();
			starlingController.setStarlingAPIService(starlingApiService);
			
			String transferUID = UUID.randomUUID().toString();
			
			starlingController.run(accessToken, args[0], args[1], transferUID);	
			
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException getting startling.properties. " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException loading input stream of startling.properties. " + e.getMessage());
		}

    }
    
}
