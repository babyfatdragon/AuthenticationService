package jp.co.workscm.ec.AuthenticationService.api;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED_TYPE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jose4j.lang.JoseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import jp.co.workscm.ec.AuthenticationService.entity.User;
import jp.co.workscm.ec.AuthenticationService.util.PasswordUtil;
import jp.co.workscm.ec.AuthenticationService.util.SimpleKeyGenerator;
@RunWith(Arquillian.class)
public class UserEndPointTest {

	private Client client;
	private WebTarget userTarget;
	
	@ArquillianResource
	private URI baseURL;
	
	@Deployment
	public static WebArchive createDepolyment() {
        // Import Maven runtime dependencies
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
        		.importRuntimeDependencies().resolve().withTransitivity().asFile();
        
        return ShrinkWrap.create(WebArchive.class)
				.addClasses(UserEndPoint.class, ApplicationConfig.class, User.class, 
						PasswordUtil.class, SimpleKeyGenerator.class, JoseException.class)
				.addAsResource("META-INF/persistence.xml", "META-INF/persistence-test.xml")
				.addAsWebResource(EmptyAsset.INSTANCE, "beans.xml")
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
}
