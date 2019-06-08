package de.htw.ai.kbe.api;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.htw.ai.kbe.services.TokenHandler;
import de.htw.ai.kbe.storage.IUsersDAO;

@Path("/auth")
public class UsersWebService {
	
    private IUsersDAO uDAO;
    private TokenHandler tk = new TokenHandler();
    
    @Inject
    public UsersWebService(IUsersDAO uDAO) {
    	this.uDAO = uDAO;
    }
	
	@GET
	@Produces({ MediaType.TEXT_PLAIN})
	public Response getToken(@QueryParam("userid") String userid, @QueryParam("secret") String pw) {
		
	
		if(isValid(userid) && isValid(pw)) {
			
			// ask DB if user exists
			
			String token = "empty token";
			
			if(uDAO.validateUser(userid, pw)) {
				token = tk.generateToken(userid, pw);
			} else return Response.status(Response.Status.BAD_REQUEST).entity("No user with this userid and password found.").header("Content-Type", "text/plain").build();
			
			
			//if(token == null || token.isEmpty()) token = "no token found";
			return Response.status(Response.Status.OK).entity(token).header("Content-Type", "text/plain").build();
		} else return Response.status(Response.Status.BAD_REQUEST).entity("Bad input. Please provide userid and password.").header("Content-Type", "text/plain").build();
	}
	
	private boolean isValid(String input) {
		if(input != null && input != "") return true;
		else return false;
	}
	

}
