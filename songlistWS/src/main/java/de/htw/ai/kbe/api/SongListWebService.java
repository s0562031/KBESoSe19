package de.htw.ai.kbe.api;

import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
	public Response getAllSongs(@QueryParam ("id") Integer id, @Context HttpHeaders headers) {
		
    	String usertoken = null;
    	String dbresponse = null;
    	List<SongList> responseSong = null;
    	
    	if(validateToken(headers) == null) return Response.status(Response.Status.NOT_FOUND).entity("Please provide your authorization token.").header("Content-Type", "application/json").build();
    	else usertoken = validateToken(headers);
    	
    	Userlist user = slDAO.getUserFromToken(usertoken);
    			
				
		// if id is usern own id
    	if(user.getUserid().equals(id))	responseSong = slDAO.getAllOwnedSongLists();
    	else responseSong = slDAO.getAllForeignSongLists();

		
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
				return Response.status(Response.Status.BAD_REQUEST).entity("Bad ID").build();
			} 
			
			return Response.status(Response.Status.OK).entity(dbresponse).header("Content-Type", "application/xml").build();
		}	
		
		else return Response.status(Response.Status.BAD_REQUEST).entity("Header not accepted").build();
	}
    
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
    	    	
    	JAXBContext context = JAXBContext.newInstance(SongWrapper.class);
    	Marshaller marshall = context.createMarshaller();
    	marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); 
    	
    	StringWriter sw = new StringWriter();
    	
    	SongListWrapper wrapper = new SongListWrapper(songList); 
    	marshall.marshal(wrapper,sw);
    	
    	return sw.toString();
    }
	
}
