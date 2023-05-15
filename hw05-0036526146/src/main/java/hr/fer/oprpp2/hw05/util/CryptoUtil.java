package hr.fer.oprpp2.hw05.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 */
public class CryptoUtil {
	
	public static String bytesToHex(byte[] arr) {
		StringBuilder sb = new StringBuilder(arr.length * 2);
		for(byte b : arr)
			sb.append(String.format("%02x", b));
		return sb.toString();
	}
	
	public static String encrypt(String string) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		byte[] bytesEncrypted = messageDigest.digest(string.getBytes());
		
		return bytesToHex(bytesEncrypted);
	}
	

}
