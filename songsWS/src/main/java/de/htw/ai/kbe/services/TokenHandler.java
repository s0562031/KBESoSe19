package de.htw.ai.kbe.services;


import java.util.Base64;
import java.util.HashMap;


public class TokenHandler {
	

	private HashMap<String, String> tokenMap;
	
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
	
	private void storeToken(String userid, String encryptedString) {
		tokenMap.put(userid, encryptedString);
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
