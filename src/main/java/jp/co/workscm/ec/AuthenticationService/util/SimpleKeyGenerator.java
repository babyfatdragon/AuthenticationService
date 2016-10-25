package jp.co.workscm.ec.AuthenticationService.util;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum SimpleKeyGenerator {
	INSTANCE;
	private static RsaJsonWebKey rsaJsonWebKey;
	static {
		try {
			rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
		} catch (JoseException e) {
			log.error("Error happens when creating rsaJsonWebKey, JoseException: " + e);
		}
	}
	
	public RsaJsonWebKey getKey() {
		return rsaJsonWebKey;
	}
}
