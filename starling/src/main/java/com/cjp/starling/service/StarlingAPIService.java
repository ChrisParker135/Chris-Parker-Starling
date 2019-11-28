package com.cjp.starling.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

/**
 * Use the Starling REST API
 * 
 * @author Chris Parker
 *
 */
public class StarlingAPIService {

	private final static String GET_REQUEST_METHOD = "GET";
	private final static String PUT_REQUEST_METHOD = "PUT";

	/**
	 * Get the Bank Accounts accountUID and default category
	 * 
	 * @param address       String API URL
	 * @param authorization String access token
	 * @return JSONObject connection response object
	 */
	public JSONObject getRequest(String address, String authorization) {

		JSONObject jsonObject = new JSONObject();

		try {

			URL url = new URL(address);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(GET_REQUEST_METHOD);
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Authorization", authorization);

			int responseCode = connection.getResponseCode();
			System.out.println("HTTP response: " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				jsonObject = convertToJSON(connection.getInputStream());
			} else {
				System.out.println("Response to get Request is not OK");
			}
			connection.disconnect();

		} catch (MalformedURLException e) {
			System.out.println("Malformed Exception with URL " + address + ". " +  e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException opening connection. " + e.getMessage());
		}

		return jsonObject;
	}

	/**
	 * 
	 * @param address
	 * @param authorization
	 * @return
	 */
	public JSONObject putRequest(String address, String authorization, String body) {

		JSONObject jsonObject = new JSONObject();

		try {

			URL url = new URL(address);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(PUT_REQUEST_METHOD);
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Authorization", authorization);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			OutputStream os = connection.getOutputStream();
			os.write(body.getBytes());
			os.flush();

			int responseCode = connection.getResponseCode();
			System.out.println("HTTP response: " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				jsonObject = convertToJSON(connection.getInputStream());
			} else {
				System.out.println("Response to put Request is not OK");
			}

			connection.disconnect();

		} catch (MalformedURLException e) {
			System.out.println("Malformed Exception with URL " + address + ". " + e.getMessage());
			return null;
		} catch (IOException e) {
			System.out.println("IOException opening connection. " + e.getMessage());
			return null;
		}

		return jsonObject;
	}

	/**
	 * Convert an InputStream to a JSONObject
	 * @param inputStream InputStream containing JSON string
	 * @return JSONObject converted JSON
	 */
	private JSONObject convertToJSON(InputStream inputStream) {

		JSONObject jsonObject = new JSONObject();

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder stringBuilder = new StringBuilder();
		bufferedReader.lines().forEach(line -> {
			stringBuilder.append(line);
		});

		System.out.println("JSON String Result " + stringBuilder.toString());

		jsonObject = new JSONObject(stringBuilder.toString());

		return jsonObject;
	}

}
