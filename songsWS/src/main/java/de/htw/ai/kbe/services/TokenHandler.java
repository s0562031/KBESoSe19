package de.htw.ai.kbe.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class TokenHandler {
	
	private String token = null;
	private HashMap<Integer, String> tokenMap;
	
	public TokenHandler() {
		tokenMap = new HashMap<Integer,String>();
	}
	
	
	public String generateToken(int userid, String pw) throws NoSuchAlgorithmException {
		
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(pw.getBytes());
		String encryptedString = new String(messageDigest.digest());
		
		storeToken(userid, encryptedString);
		
		return token;
	}
	
	private void storeToken(int userid, String encryptedString) {
		tokenMap.put(userid, encryptedString);
	}

}
