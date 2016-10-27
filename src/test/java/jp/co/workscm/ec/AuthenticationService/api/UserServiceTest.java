package jp.co.workscm.ec.AuthenticationService.api;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URI;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
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
import org.jose4j.lang.JoseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import jp.co.workscm.ec.AuthenticationService.TestUtil;
import jp.co.workscm.ec.AuthenticationService.entity.User;
import jp.co.workscm.ec.AuthenticationService.util.PasswordUtil;
import jp.co.workscm.ec.AuthenticationService.util.SimpleKeyGenerator;
@RunWith(Arquillian.class)
@RunAsClient
public class UserServiceTest {

	private Client client;
	private WebTarget userTarget;
    private User testUser = new User(1L, "testUser", "testPassword", "John", "Doe");
	
	@ArquillianResource
	private URI baseURL;
	
	@Deployment
	public static WebArchive createDepolyment() {
        // Import Maven runtime dependencies
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
        		.importRuntimeDependencies().resolve().withTransitivity().asFile();
        
        return ShrinkWrap.create(WebArchive.class)
				.addClasses(ApplicationConfig.class, UserService.class, User.class)
				.addClasses(PasswordUtil.class, SimpleKeyGenerator.class, JoseException.class)
				.addAsResource("META-INF/persistence-test.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(files);
	}
	
	@Before
	public void init() {
		client = ClientBuilder.newClient();
		userTarget = client.target(baseURL).path("api/user");
	}
	
	@Test
	public void shouldFailLogin() {
		Form loginForm = new Form();
		loginForm.param("username", "dummyUsername");
		loginForm.param("password", "dummyPassword");
		Response response = userTarget.path("login").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(loginForm, APPLICATION_FORM_URLENCODED_TYPE));
		assertThat(response.getStatus(), is(401));
		assertThat(response.getHeaderString(AUTHORIZATION), is(nullValue()));
	}
	
	@Test
	@InSequence(1)
	public void shouldGetAllUsers() {
		Response response = userTarget.request(APPLICATION_JSON_TYPE).get();
		assertThat(response.getStatus(), is(200));
	}
	
	@Test
	@InSequence(2)
	public void shouldCreateAnUser() {
		Response response = userTarget.request(APPLICATION_JSON_TYPE).post(Entity.entity(testUser,  APPLICATION_JSON_TYPE));
		assertThat(response.getStatus(), is(201));
	}
	
	@Test
	@InSequence(3)
	public void shouldGetCreatedUser() {
		Response response = userTarget.path(String.valueOf((testUser.getId()))).request(APPLICATION_JSON_TYPE).get();
		assertThat(response.getStatus(), is(200));
		JsonObject jsonObject = TestUtil.readJsonContent(response);
		assertThat(jsonObject.getJsonNumber("id").longValue(), is(testUser.getId()));
		assertThat(jsonObject.getString("lastName"), is(testUser.getLastName()));
	}
	
	@Test
	@InSequence(4)
	public void shouldRemoveUser() {
		Response response = userTarget.path(String.valueOf(testUser.getId())).request(APPLICATION_JSON_TYPE).delete();
		assertThat(response.getStatus(), is(204));
		Response secondResponse = userTarget.path(String.valueOf(testUser.getId())).request(APPLICATION_JSON_TYPE).get();
		assertThat(secondResponse.getStatus(), is(404));
	}
	

	
	
}
