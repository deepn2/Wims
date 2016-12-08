package com.group4inc.wims.idm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordOps {
	
	
	//http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
	public static String passwordCrypt(String toCrypt) {
		String out = null;
		
		try {
			MessageDigest mdfive = MessageDigest.getInstance("MD5");
			mdfive.update(toCrypt.getBytes());
			byte[] hashbites = mdfive.digest();
			StringBuilder intoString = new StringBuilder();
			
			for(int i=0; i<hashbites.length; i++) {
				intoString.append(Integer.toString((hashbites[i] & 0xff) + 0x100, 16).substring(1));
			}
			out = intoString.toString();
		}
		catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public static boolean comparePasswords(String actual, String attempt) {
		if(actual.equals(passwordCrypt(attempt)))
			return true;
		else
			return false;
	}
}
