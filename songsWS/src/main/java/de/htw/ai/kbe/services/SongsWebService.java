package de.htw.ai.kbe.services;

import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import de.htw.ai.kbe.bean.Songs;
import de.htw.ai.kbe.storage.InMemoryAddressBook;

// URL fuer diesen Service ist: 
//http://localhost:8080/songsWS/rest/songs 
@Path("/songs")
public class SongsWebService {

	// Singleton Pattern
    private InMemoryAddressBook addressBook = InMemoryAddressBook.getInstance();
    
    //GET http://localhost:8080/songsWS/rest/songs
	@GET 
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML }) // JSON an erster Stelle ist default
	public Collection<Songs> getAllSongs() {
		System.out.println("getAllSongs: Returning all songs!");
		return addressBook.getAllSongs(); // Collection von POJOs
	}

    //GET http://localhost:8080/songsWS/rest/songs/1
	//Returns: 200 & contact with id 1 or 404 when id not found, 
	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getSong(@PathParam("id") Integer id) {
		Songs song = addressBook.getSong(id);
		if (song != null) {
			System.out.println("getSong: Returning song for id " + id);
			return Response.status(Response.Status.OK).entity(song).build();
		} else {
		    return Response.status(Response.Status.NOT_FOUND).entity("ID not found").build();
		}
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
	public Response createContact(Songs song) {
	     Integer newId = addressBook.addSong(song);
	     UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
	     uriBuilder.path(Integer.toString(newId));
	     return Response.created(uriBuilder.build()).build();
	}
    	
	@PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/{id}")
    public Response updateSong(@PathParam("id") Integer id, Songs song) {
        return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity("PUT not implemented").build();
    }

	@DELETE
	@Path("/{id}")
	public Response deleteSong(@PathParam("id") Integer id) {
		return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity("DELETE not implemented").build();
	}
}
