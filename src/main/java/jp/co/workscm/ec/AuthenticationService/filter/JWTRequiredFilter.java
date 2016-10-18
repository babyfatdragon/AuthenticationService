package jp.co.workscm.ec.AuthenticationService.filter;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.io.IOException;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author li_zh
 *
 */
@Provider
@JWTRequired
@Slf4j
public class JWTRequiredFilter implements ContainerRequestFilter {
	
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		log.info("authorizationHeader: " + authorizationHeader);
		
		if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			log.error("Invalid authorizationHeader");
			throw new NotAuthorizedException("Authorization Header must be provided.");
		}
		
		String jwt = authorizationHeader.substring("Bearer".length()).trim();
		
		//validate the jwt
		try {
			RsaJsonWebKey rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
			JwtConsumer jwtConsumer = new JwtConsumerBuilder()
					.setRequireExpirationTime()
					.setMaxFutureValidityInMinutes(100)
					.setAllowedClockSkewInSeconds(30)
					.setRequireSubject()
					.setExpectedIssuer("WORKS APPLICATIONS")
					.setExpectedAudience("Audience")
					.setVerificationKey(rsaJsonWebKey.getPublicKey())
					.build();
			try {
				JwtClaims jwtClaims =jwtConsumer.processToClaims(jwt);
				log.info("Valid JWT: " + jwt);
			} catch (InvalidJwtException e) {
				System.out.println("Invalid JWT. " + e);
			}
		} catch(Exception e) {
			log.error("Invalid JWT: " + jwt);
			requestContext.abortWith(Response.status(UNAUTHORIZED).build());
		}
	}
}
