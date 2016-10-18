package jp.co.workscm.ec.AuthenticationService.util;

import java.security.MessageDigest;
import java.util.Base64;
/**
 * 
 * @author li_zh
 *
 */
public class PasswordUtil {

	public static String digestPassword(String plainTextPassword) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(plainTextPassword.getBytes("UTF-8"));
			byte[] passwordDigest = md.digest();
			return new String(Base64.getEncoder().encode(passwordDigest));
		} catch (Exception e) {
			throw new RuntimeException("Errors in hashing password. " + e);
		}
	}
}
