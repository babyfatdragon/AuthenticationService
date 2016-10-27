package jp.co.workscm.ec.AuthenticationService.api;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import jp.co.workscm.ec.AuthenticationService.entity.User;
import jp.co.workscm.ec.AuthenticationService.util.PasswordUtil;
import jp.co.workscm.ec.AuthenticationService.util.SimpleKeyGenerator;

/**
 * 
 * @author li_zh
 *
 */
@Path("/user")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
public class UserService {
	@Context
	private UriInfo uriInfo;
	
	@PersistenceContext(unitName="userPU")
	private EntityManager em;
	
	@POST
	public Response create(User user) {
		user.setPassword(PasswordUtil.digestPassword(user.getPassword()));
		System.out.println("MIMA " + user.getPassword());
		em.persist(user);
		return Response.created(uriInfo
				.getAbsolutePathBuilder()
				.path(String.valueOf(user.getId())).build())
				.build();
	}
	
	@GET
	@Path("/{id}")
	public Response findById(@PathParam("id") String id) {
		User user = em.find(User.class, Long.parseLong(id));
		if(user == null) {
			return Response.status(NOT_FOUND).build();
		}
		return Response.ok(user).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response remove(@PathParam("id") String id) {
		em.remove(em.getReference(User.class, Long.parseLong(id)));
		return Response.noContent().build();
	}
	
	@GET
	public Response findAllUsers() {
		TypedQuery<User> query = em.createNamedQuery(User.FIND_ALL, User.class);
		List<User> users = query.getResultList();
		if(users == null) {
			return Response.status(NOT_FOUND).build();
		}
		return Response.ok(users).build();
	}

	@POST
	@Path("/login")
	@Consumes(APPLICATION_FORM_URLENCODED)
	public Response login(@FormParam("username") String username, 
			@FormParam("password") String password) {
		if(username==null || username=="") {
			return Response.status(Status.PRECONDITION_FAILED).build();
		}
		try {
			authenticate(username, password);	
			String jwt = issueToken(username);
			return Response.ok().header(AUTHORIZATION, "Bearer " + jwt) .build();	
		} catch (Exception e) {
			return Response.status(UNAUTHORIZED).build();
		}
	}

	private void authenticate(String username, String password) {
		// TODO Auto-generated method stub
		TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_USERNAME_AND_PASSWORD, User.class);
		query.setParameter("username", username);
		query.setParameter("password", PasswordUtil.digestPassword(password));
		User user = query.getSingleResult();
		if(user == null) {
			throw new SecurityException("Invalid credentials");
		}
	}
	
	private String issueToken(String username) throws JoseException {
		RsaJsonWebKey rsaJsonWebKey = SimpleKeyGenerator.INSTANCE.getKey();
		rsaJsonWebKey.setKeyId("k1");
		JwtClaims claims = new JwtClaims();
		claims.setGeneratedJwtId();
		claims.setSubject(username);
		claims.setIssuer("WORKS APPLICATIONS");
		claims.setAudience("Audience");
		claims.setIssuedAtToNow();
		claims.setExpirationTimeMinutesInTheFuture(10);
		
		JsonWebSignature jws = new JsonWebSignature();
		jws.setPayload(claims.toJson());
		jws.setKey(rsaJsonWebKey.getPrivateKey());
		jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
		jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
		
		String jwt = jws.getCompactSerialization();
		
		return jwt;
	}
}
