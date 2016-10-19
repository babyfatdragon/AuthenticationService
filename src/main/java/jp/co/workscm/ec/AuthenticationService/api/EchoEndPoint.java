package jp.co.workscm.ec.AuthenticationService.api;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * 
 * @author li_zh
 *
 */
@Path("/echo")
@Produces(TEXT_PLAIN)
public class EchoEndPoint {
	
	@GET
	public Response echo(@QueryParam("message") String message) {
		return Response.ok().entity(message == null ? "no message" : message).build();
	}
	
	@GET
	@Path("/jwt")
	public Response echoWithJWT(@QueryParam("message") String message) {
		return Response.ok().entity(message == null ? "no message" : message).build();
	}
}
