package jp.co.workscm.ec.AuthenticationService.api;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import jp.co.workscm.ec.AuthenticationService.entity.User;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Path("/user")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class UserEndPoint {
	@Context
	private UriInfo uriInfo;
	@Inject
	private EntityManager em;
	
	@POST
	@Path("/login")
	@Produces(APPLICATION_JSON)
	public Response login(@HeaderParam("mlAddress") String mlAddress, 
			@HeaderParam("password") String password) {
		if(mlAddress==null || mlAddress=="") {
			return Response.status(Status.PRECONDITION_FAILED).build();
		}
		try {
			authenticate(mlAddress, password);
			String jwt = "samplejwt.samplejwt.samplejwt";
			return Response.ok().header(AUTHORIZATION, "Bearer " + jwt) .build();	
		} catch (Exception e) {
			return Response.status(UNAUTHORIZED).build();
		}
		
	}

	private void authenticate(String mlAddress, String password) {
		// TODO Auto-generated method stub
		TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_ML_ADDRESS_AND_PASSWORD, User.class);
		query.setParameter("mlAddress", mlAddress);
		query.setParameter("password", password);
		User user = query.getSingleResult();
		if(user == null) {
			throw new SecurityException("Invalid credentials");
		}
	}
}
