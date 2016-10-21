package jp.co.workscm.ec.AuthenticationService.util;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;

public enum SimpleKeyGenerator {
	INSTANCE;
	private static RsaJsonWebKey rsaJsonWebKey;
	static {
		try {
			rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
		} catch (JoseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public RsaJsonWebKey getKey() {
		return rsaJsonWebKey;
	}
}
