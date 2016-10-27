package jp.co.workscm.ec.AuthenticationService.api;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import jp.co.workscm.ec.AuthenticationService.filter.JWTRequired;

@RunWith(Arquillian.class)
@RunAsClient
public class EchoServiceTest {
	
	private Client client;
	private WebTarget echoTarget;
	
	@ArquillianResource 
	private URI baseURL;
	
	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class)
				.addClasses(ApplicationConfig.class, EchoService.class, JWTRequired.class)
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Before
	public void init() {
		client = ClientBuilder.newClient();
		echoTarget = client.target(baseURL).path("/api/echo");
	}
	
	@Test
	public void shouldEchoNoMessage() {
		Response response = echoTarget.request(TEXT_PLAIN).get();
		assertThat(response.getStatus(), is(200));
		assertThat(response.readEntity(String.class), is("no message"));
	}
	
	@Test
	public void shouldEchoHello() {
		Response response = echoTarget.queryParam("message", "hello").request(TEXT_PLAIN).get();
		assertThat(response.getStatus(), is(200));
		assertThat(response.readEntity(String.class), is("hello"));
	}
}
