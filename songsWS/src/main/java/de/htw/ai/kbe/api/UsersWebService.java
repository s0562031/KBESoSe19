package de.htw.ai.kbe.api;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import de.htw.ai.kbe.storage.IUsersDAO;

@Path("/auth")
public class UsersWebService {
	
    private IUsersDAO uDAO;
    
    @Inject
    public UsersWebService(IUsersDAO uDAO) {
    	this.uDAO = uDAO;
    }
	
	@GET
	@Produces({ MediaType.TEXT_PLAIN})
	public Response getToken(@PathParam("userid") String userid, @PathParam("secret") String pw) {
		
		String token = uDAO.getToken(userid, pw);
		return Response.status(Response.Status.OK).entity(token).header("Content-Type", "text/plain").build();
	}
	

}
