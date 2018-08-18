package com.tw;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.request.ChallengeOneDto;

@SpringBootApplication
public class MainTWChallenge {

	private static String baseUrl = "https://http-hunt.thoughtworks-labs.net";
	public static final String smallAlpha = "abcdefghijklmnopqrstuvwxyz";
	public static final String capAlpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static final String userIdValue = "aG2BgaTxS";
	
	public static final String userIdKey = "userId";
	
	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper();
		
		//SpringApplication.run(MainTWChallenge.class, args);
		MainTWChallenge mainTW = new MainTWChallenge();
		try {
			TypeReference<List<ChallengeOneDto>> mapType = new TypeReference<List<ChallengeOneDto>>() {};
			List<ChallengeOneDto> jsonToPersonList = mapper.readValue(mainTW.s1InputCall(), mapType);
			System.out.println(jsonToPersonList.size());
			//s1OutputCall("{\"count\":\"" + jsonToPersonList.size() + "\"}");
			for(ChallengeOneDto challDto : jsonToPersonList) {
				System.out.println(challDto.getName());
				System.out.println(challDto.getPrice());
				System.out.println(challDto.getCategory());
				System.out.println("End Date"+challDto.getEndDate());
				System.out.println("Start Date: "+challDto.getStartDate());
				System.out.println("---------------------------------");
				
			}
			//jsonToPersonList.forEach(System.out::println);
			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private static String decrypt(String encryptedMsg, Long key) {
		String decryptedMsg = "";
		for (int i = 0; i < encryptedMsg.length(); i++) {
			if (Character.isAlphabetic(encryptedMsg.charAt(i))) {
				if (Character.isUpperCase(encryptedMsg.charAt(i))) {
					int charPosition = capAlpha.indexOf(encryptedMsg.charAt(i));
					int keyVal = (int) ((charPosition - key) % 26);
					if (keyVal < 0) {
						keyVal = capAlpha.length() + keyVal;
					}
					char ch = capAlpha.charAt(keyVal);
					decryptedMsg = decryptedMsg + ch;
				} else {
					int charPosition = smallAlpha.indexOf(encryptedMsg.charAt(i));
					int keyVal = (int) ((charPosition - key) % 26);
					if (keyVal < 0) {
						keyVal = smallAlpha.length() + keyVal;
					}
					char ch = smallAlpha.charAt(keyVal);
					decryptedMsg = decryptedMsg + ch;
					decryptedMsg = decryptedMsg + ch;
				}
			} else {
				decryptedMsg = decryptedMsg + encryptedMsg.charAt(i);
			}
		}
		return decryptedMsg;
	}
	
	private static void s1OutputCall(String strResponse) {
		try {
			URL url = new URL(baseUrl + "/challenge/output");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("userId", userIdValue);
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(strResponse);
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			if (responseCode == 200) {
				System.out.println("\n*-*-*-* Success Response *-*-*-*\n" + response);
			} else {
				System.out.println("\n*-*-*-* Failed Response *-*-*-*\n" + response);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String s1InputCall() {
		try {
			URL url = new URL(baseUrl + "/challenge/input");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("userId", userIdValue);
			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			if (responseCode == 200) {
				System.out.println("\n*-*-*-* Success Response *-*-*-*\n" + response);
			} else {
				System.out.println("\n*-*-*-* Failed Response *-*-*-*\n" + response);
				return null;
			}
			in.close();
			return response.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
