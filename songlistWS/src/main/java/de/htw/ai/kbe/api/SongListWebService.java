package de.htw.ai.kbe.api;

import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import de.htw.ai.kbe.data.SongList;
import de.htw.ai.kbe.data.SongListWrapper;
import de.htw.ai.kbe.data.SongWrapper;
import de.htw.ai.kbe.data.Songs;
import de.htw.ai.kbe.data.Userlist;
import de.htw.ai.kbe.services.TokenHandler;
import de.htw.ai.kbe.storage.ISongListDAO;
import de.htw.ai.kbe.storage.ISongsDAO;
import de.htw.ai.kbe.storage.IUsersDAO;

@Path("/songlist")
public class SongListWebService {

	
    private ISongListDAO slDAO;
    private IUsersDAO uDAO;
    private TokenHandler th = TokenHandler.getInstance();
    
    @Inject
    public SongListWebService(ISongListDAO sDAO, IUsersDAO uDAO) {
    	this.slDAO = sDAO;
    	this.uDAO = uDAO;
    }
    
    
    @GET 
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML  }) // JSON an erster Stelle ist default
	public Response getAllSongLists(@QueryParam ("userId") String id, @Context HttpHeaders headers) {
		
    	String usertoken = null;
    	String dbresponse = null;
    	List<SongList> responseSong = null;
    	
    	// # validate params
    	if(id == null || id.isEmpty()) return Response.status(Response.Status.NOT_FOUND).entity("Please provide an userid.").header("Content-Type", "application/json").build();
    	if(validateToken(headers) == null) return Response.status(Response.Status.NOT_FOUND).entity("Please provide your authorization token.").header("Content-Type", "application/json").build();
    	else usertoken = validateToken(headers);
    	
    	// # get user from token to check if he/she wants her/his own songlists or others
    	String userid = slDAO.getUserFromToken(usertoken);
    	if(userid.isEmpty()) return Response.status(Response.Status.NOT_FOUND).entity("No user found for this token.").header("Content-Type", "application/json").build();	
				
		// if id is usern own id
    	if(userid.equals(id))	responseSong = slDAO.getAllOwnedSongLists(id);
    	else responseSong = slDAO.getAllForeignSongLists(id);

		
		if(responseSong == null) return Response.status(Response.Status.NOT_FOUND).entity("ID not found").build();
		
		
		// ## build awnser
		List<MediaType> acceptableTypes = headers.getAcceptableMediaTypes();	
		
		// JSON is standard, if no or both types are given use it
		if(acceptableTypes.contains(MediaType.WILDCARD_TYPE)) acceptableTypes.add(MediaType.APPLICATION_JSON_TYPE);
		
		if(acceptableTypes.contains(MediaType.APPLICATION_JSON_TYPE)) {
			try {
				dbresponse = pojoToJSON(responseSong);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).entity("Bad ID").build();
			} 
			
			return Response.status(Response.Status.OK).entity(dbresponse).header("Content-Type", "application/json").build();

		} 
		
		else if(acceptableTypes.contains(MediaType.APPLICATION_XML_TYPE) && !acceptableTypes.contains(MediaType.APPLICATION_JSON_TYPE)) {
			try {
				dbresponse = pojoListToXML(responseSong);
			} catch (JAXBException e) {
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).entity("XML parsing error.").build();
			} 
			
			return Response.status(Response.Status.OK).entity(dbresponse).header("Content-Type", "application/xml").build();
		}	
		
		else return Response.status(Response.Status.BAD_REQUEST).entity("Header not accepted").build();
	}
    
    
    
    @GET 
    @Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML  }) // JSON an erster Stelle ist default
	public Response getSong(@PathParam ("id") Integer songlistid, @Context HttpHeaders headers) {
		
    	String usertoken = null;
    	String dbresponse = null;
    	SongList responseSong = null;
    	
    	// # validate params
    	if(songlistid == null) return Response.status(Response.Status.NOT_FOUND).entity("Please provide a songlistid.").header("Content-Type", "application/json").build();
    	if(validateToken(headers) == null) return Response.status(Response.Status.NOT_FOUND).entity("Please provide your authorization token.").header("Content-Type", "application/json").build();
    	else usertoken = validateToken(headers);
    	
    	// # compare user from token to owner of songlist
    	String userid = slDAO.getUserFromToken(usertoken);
    	if(userid.isEmpty()) return Response.status(Response.Status.NOT_FOUND).entity("No user found for this token.").header("Content-Type", "application/json").build();	
				
		// if id is usern own id
    	String songlistowner = slDAO.getSongListOwner(songlistid);
    	if(songlistowner.contentEquals(userid))responseSong = slDAO.getOwnedSongList(userid, songlistid);
    	else responseSong = slDAO.getForeignSongList(songlistid);

		
		if(responseSong == null) return Response.status(Response.Status.NOT_FOUND).entity("No entry found").build();
		
		
		// ## build awnser
		List<MediaType> acceptableTypes = headers.getAcceptableMediaTypes();	
		
		// JSON is standard, if no or both types are given use it
		if(acceptableTypes.contains(MediaType.WILDCARD_TYPE)) acceptableTypes.add(MediaType.APPLICATION_JSON_TYPE);
		
		if(acceptableTypes.contains(MediaType.APPLICATION_JSON_TYPE)) {
			try {
				dbresponse = pojoToJSON(responseSong);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).entity("Bad ID").build();
			} 
			
			return Response.status(Response.Status.OK).entity(dbresponse).header("Content-Type", "application/json").build();

		} 
		
		else if(acceptableTypes.contains(MediaType.APPLICATION_XML_TYPE) && !acceptableTypes.contains(MediaType.APPLICATION_JSON_TYPE)) {
			try {
				dbresponse = pojoToXML(responseSong);
			} catch (JAXBException e) {
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).entity("Bad ID").build();
			} 
			
			return Response.status(Response.Status.OK).entity(dbresponse).header("Content-Type", "application/xml").build();
		}	
		
		else return Response.status(Response.Status.BAD_REQUEST).entity("Header not accepted").build();
    	
	}
  

    
    @DELETE 
    @Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML  }) // JSON an erster Stelle ist default
	public Response deleteSong(@PathParam ("id") Integer songlistid, @Context HttpHeaders headers) {
		
    	String usertoken = null;
    	
    	// # validate params
    	if(songlistid == null) return Response.status(Response.Status.NOT_FOUND).entity("Please provide a songlistid.").header("Content-Type", "application/json").build();
    	if(validateToken(headers) == null) return Response.status(Response.Status.NOT_FOUND).entity("Please provide your authorization token.").header("Content-Type", "application/json").build();
    	else usertoken = validateToken(headers);
    	
    	// # compare user from token to owner of songlist
    	String userid = slDAO.getUserFromToken(usertoken);
    	if(userid.isEmpty()) return Response.status(Response.Status.NOT_FOUND).entity("No user found for this token.").header("Content-Type", "application/json").build();	
				
		// if id is usern own id
    	String songlistowner = slDAO.getSongListOwner(songlistid);
    	
    	if(songlistowner.contentEquals(userid)) {
    		slDAO.deleteSongList(songlistid);
    		return Response.status(Response.Status.OK).entity("SongList + " + songlistid + " deleted.").build();
    	}
    	else return Response.status(Response.Status.FORBIDDEN).entity("You are not allowed to delte others playlists.").build();
		
	}
  
    /**
     * Checks if token from HttpHeader is not empty and present for any user in DB.
     * 
     * @param HttpHeaders headers
     * @return String of token from authorize header.
     */
    private String validateToken(HttpHeaders headers) {
    	
    	String authtoken = "thisisnotatoken";
		if(headers.getRequestHeader("Authorization") != null) {
			authtoken = headers.getRequestHeader("Authorization").get(0);
		} else return null;
			
		if(!uDAO.validateToken(authtoken)) 	return null;
		else return authtoken;
    	
  		
    }
    
    
    /**
     * Maps POJO song objects to JSON.
     * 
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    private static String pojoToJSON(Object obj) throws JsonProcessingException {
    	
    	ObjectWriter mapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
    	return mapper.writeValueAsString(obj);
    }
    
    /**
     * 
     * @throws JAXBException
     * @throws FileNotFoundException 
     */
    private String pojoToXML(Object obj) throws JAXBException {
    	
    	//System.out.println("filename:" + filename);
    	
    	JAXBContext context = JAXBContext.newInstance(obj.getClass());
    	Marshaller marshall = context.createMarshaller();
    	marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); 
    	
    	StringWriter sw = new StringWriter();
    	
    	marshall.marshal(obj,sw);
    	
    	return sw.toString();
    }
    
    private String pojoListToXML(List<SongList> songList) throws JAXBException {
    	    	
    	JAXBContext context = JAXBContext.newInstance(SongListWrapper.class);
    	Marshaller marshall = context.createMarshaller();
    	marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); 
    	
    	StringWriter sw = new StringWriter();
    	
    	SongListWrapper wrapper = new SongListWrapper(songList); 
    	marshall.marshal(wrapper,sw);
    	
    	return sw.toString();
    }
	
}
