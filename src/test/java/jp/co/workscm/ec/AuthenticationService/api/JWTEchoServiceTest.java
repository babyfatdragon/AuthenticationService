package jp.co.workscm.ec.AuthenticationService.api;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URI;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import jp.co.workscm.ec.AuthenticationService.entity.User;
import jp.co.workscm.ec.AuthenticationService.filter.JWTRequired;
import jp.co.workscm.ec.AuthenticationService.filter.JWTRequiredFilter;
import jp.co.workscm.ec.AuthenticationService.util.PasswordUtil;
import jp.co.workscm.ec.AuthenticationService.util.SimpleKeyGenerator;

@RunWith(Arquillian.class)
@RunAsClient
public class JWTEchoServiceTest {

	private Client client;
	private WebTarget userTarget;
	private WebTarget echoTarget;
    private static final User testUser = new User(1L, "testUser", "testPassword", "John", "Doe");
	private static String authorizationHeader;
	@ArquillianResource
	private URI baseURL;
	
	@Deployment
	public static WebArchive createDeployment() {
		File[] files = Maven.resolver().loadPomFromFile("pom.xml")
				.importRuntimeDependencies().resolve().withTransitivity().asFile();
		
		return ShrinkWrap.create(WebArchive.class)
				.addClasses(ApplicationConfig.class, EchoService.class, UserService.class, User.class)
				.addClasses(JWTRequired.class, JWTRequiredFilter.class)
				.addClasses(PasswordUtil.class, SimpleKeyGenerator.class, JoseException.class)
				.addAsResource("META-INF/persistence-test.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsLibraries(files);
	}
	
	@Before
	public void init() {
		client = ClientBuilder.newClient();
		userTarget = client.target(baseURL).path("/api/user");
		echoTarget = client.target(baseURL).path("/api/echo/jwt");
	}
	
	@Test
	@InSequence(1)
	public void echoShouldFailCauseNoJWT() {
		try {
			Response response = echoTarget.request(TEXT_PLAIN).get();
			assertThat(response.getStatus(), is(401));
		} catch (Exception e){
			assertThat(e instanceof NotAuthorizedException, is(true));
		}
	}
	
	@Test
	@InSequence(2)
	public void shouldCreateAnUser() {
	Response response = userTarget.request(APPLICATION_JSON_TYPE).post(Entity.entity(testUser, APPLICATION_JSON_TYPE));
	assertThat(response.getStatus(), is(201));
	}
	
	@Test
	@InSequence(3)
	public void shouldLogUserIn() {
		Form loginForm = new Form();
		loginForm.param("username", testUser.getUsername());
		loginForm.param("password", testUser.getPassword());
		Response response = userTarget.path("login").request(APPLICATION_JSON_TYPE).post(Entity.entity(loginForm, APPLICATION_FORM_URLENCODED_TYPE));
		assertThat(response.getStatus(), is(200));
		authorizationHeader = response.getHeaderString(HttpHeaders.AUTHORIZATION);
		assertThat(authorizationHeader, is(not(nullValue())));
		assertThat(authorizationHeader.startsWith("Bearer "), is(true));
		
		String jwt = authorizationHeader.substring("Bearer".length()).trim();
		RsaJsonWebKey rsaJsonWebKey = SimpleKeyGenerator.INSTANCE.getKey();

		JwtConsumer jwtConsumer = new JwtConsumerBuilder()
				.setRequireExpirationTime()
				.setMaxFutureValidityInMinutes(100)
				.setAllowedClockSkewInSeconds(30)
				.setRequireSubject()
				.setExpectedIssuer("WORKS APPLICATIONS")
				.setExpectedAudience("Audience")
				.setVerificationKey(rsaJsonWebKey.getKey())
				.build();
		try {
			JwtClaims jwtClaims =jwtConsumer.processToClaims(jwt);
			assertThat(jwtClaims.getClaimValue("sub"), is(testUser.getUsername()));
		} catch (Exception e) {
			assertThat(e instanceof InvalidJwtException, is(true));
		}
	}
	
	@Test
	@InSequence(4)
	public void echoShouldSuccessWithJWT() {
		Response response = echoTarget.request(TEXT_PLAIN)
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader) .get();
		assertThat(response.getStatus(), is(200));
		assertThat(response.readEntity(String.class), is("no message"));
	}
	
	@Test
	@InSequence(5)
	public void shouldEchoHello() {
		Response response = echoTarget.queryParam("message", "hello").request(TEXT_PLAIN)
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
		assertThat(response.getStatus(), is(200));
		assertThat(response.readEntity(String.class), is("hello"));
	}
}
