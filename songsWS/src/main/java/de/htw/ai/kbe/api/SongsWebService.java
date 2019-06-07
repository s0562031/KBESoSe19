package de.htw.ai.kbe.api;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import de.htw.ai.kbe.data.SongList;
import de.htw.ai.kbe.data.Songs;
import de.htw.ai.kbe.storage.ISongsDAO;
import de.htw.ai.kbe.storage.InMemorySongsDB;
import de.htw.ai.kbe.storage.SongsDBDAO;

// URL fuer diesen Service ist: 
//http://localhost:8080/songsWS/rest/songs 
@Path("/songs")
public class SongsWebService {

	// Singleton Pattern
	// tight Coupling
	// user interface --> IMAB implements interface
	//* zum testen nutzen *//
    //private InMemorySongsDB addressBook = InMemorySongsDB.getInstance();
    //private SongsDBDAO sDAO= new SongsDBDAO();
    private ISongsDAO sDAO;
    
    @Inject
    public SongsWebService(ISongsDAO sDAO) {
    	this.sDAO = sDAO;
    }
    
    //GET http://localhost:8080/songsWS/rest/songs
//	@GET 
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML }) // JSON an erster Stelle ist default
//	public Collection<Songs> getAllSongs() {
//		System.out.println("getAllSongs: Returning all songs!");
//		return addressBook.getAllSongs(); // Collection von POJOs
//	}
	
	@GET 
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML  }) // JSON an erster Stelle ist default
	public Response getAllSongs(@Context HttpHeaders headers) {
		
		String dbresponse = null;
		List<MediaType> acceptableTypes = headers.getAcceptableMediaTypes();
		
		List<Songs> responseSong = sDAO.getAllSongs();
		if(responseSong == null) return Response.status(Response.Status.NOT_FOUND).entity("ID not found").build();
		
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

    //GET http://localhost:8080/songsWS/rest/songs/1
	//Returns: 200 & contact with id 1 or 404 when id not found, 
	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML  })
	public Response getSong(@PathParam("id") Integer id, @Context HttpHeaders headers) {
		
		String dbresponse = null;
		List<MediaType> acceptableTypes = headers.getAcceptableMediaTypes();
		
		Songs responseSong = sDAO.getSong(id);
		if(responseSong == null) return Response.status(Response.Status.NOT_FOUND).entity("Song " + id + " not found.").build();
		
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
	


	//Returns: 200 and 204 on provided id not found
//	@GET
//  @Path("/{id}")
//  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	public Contact getSong(@PathParam("id") Integer id) {
//        return addressBook.getSong(id);
//    }
	
	
//  POST http://localhost:8080/songsWS/rest/songs with song in payload
//  Status Code 201 und URI fuer den neuen Eintrag im http-header 'Location' zurueckschicken, also:
//  Location: /songsWS/rest/songs/neueID
	@Context 
	UriInfo uriInfo;
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	public Response createSong(Songs song) {
	     Integer newId = sDAO.addSong(song);
	     UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
	     uriBuilder.path(Integer.toString(newId));
	     return Response.created(uriBuilder.build()).build();
	}
    	
	@PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/{id}")
    public Response updateSong(@PathParam("id") Integer id, Songs song) {
		
		if ((sDAO.updateSong(song, id))){
            return Response.status(Response.Status.NO_CONTENT).entity("Song " + id + " updated.").build();
        }
            
		return Response.status(Response.Status.NOT_FOUND).entity("Song " + id + " not found.").build();        
    }

	@DELETE
	@Path("/{id}")
	public Response deleteSong(@PathParam("id") Integer id) {
		
		if (sDAO.deleteSong(id)) {
            return Response.status(Response.Status.NO_CONTENT).entity("Song " + id + " deleted.").build();
        }
		
        return Response.status(Response.Status.NOT_FOUND).entity("Song " + id + " not found.").build();
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
    
    private String pojoListToXML(List<Songs> songList) throws JAXBException {
    	    	
    	JAXBContext context = JAXBContext.newInstance(SongList.class);
    	Marshaller marshall = context.createMarshaller();
    	marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); 
    	
    	StringWriter sw = new StringWriter();
    	
    	SongList wrapper = new SongList(songList); 
    	marshall.marshal(wrapper,sw);
    	
    	return sw.toString();
    }
}
