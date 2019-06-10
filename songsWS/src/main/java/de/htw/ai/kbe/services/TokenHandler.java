package de.htw.ai.kbe.services;


import java.util.Base64;
import java.util.HashMap;

import javax.inject.Singleton;

@Singleton
public class TokenHandler {
	

	private HashMap<String, String> tokenMap = new HashMap<String, String>();
	
	public TokenHandler() {
		tokenMap = new HashMap<String,String>();
	}
	
	
	public String generateToken(String userid, String pw)  {
		
		String encryptedString = encrypt(pw);		
		// old value might be overwritten
		storeToken(userid, encryptedString);
		return encryptedString;
	}
	
	public String getTokenByID(String id) {
		return tokenMap.get(id);
//		String encrypted = tokenMap.get(id);
//		return decrypt(encrypted);
	}
	
	
	public boolean findToken(String token) {
		System.out.println("Looking for: " + token);
		System.out.println("Having token for password123: " + tokenMap.get("password123"));
		if(tokenMap.containsKey(token)) return true;
		else return false;
	}
	
	private void storeToken(String userid, String encryptedString) {
		
		tokenMap.put(userid, encryptedString);
		System.out.println("Token " + tokenMap.get(userid) + " stored.");
	}
	
	private String encrypt(String pw) {
		String encodedString = Base64.getEncoder().encodeToString(pw.getBytes());
		return encodedString;		
	}
	
	private String decrypt(String encodedString) {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
		String decodedString = new String(decodedBytes);
		
		String msg = "token generation failed";
		if(decodedString == null || decodedString == "") return msg;
		return decodedString;
	}
	
}
