package de.htw.ai.kbe.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;

import de.htw.ai.kbe.data.Songs;
import de.htw.ai.kbe.storage.ISongsDAO;
import de.htw.ai.kbe.storage.SongsDBDAO;


class SongsWebServiceTest extends JerseyTest {
	
	 	SongsDBDAO sDao;
	 
	    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("songDB-PU");

	    @Override
	    public Application configure() {
	    	return new ResourceConfig(SongsWebService.class).register(new AbstractBinder() {
	            @Override
	            protected void configure() {
	                bind(emf).to(EntityManagerFactory.class);

	                bind(SongsDBDAO.class)
	                        .to(ISongsDAO.class)
	                        .in(Singleton.class);
	            }
	        });
	    }
	    
	    @Test
		public void getAllSongs_OK_Test() {
	    	
	    	Response response = target("/songs/").request().get();
			assertEquals("should return status 200", Response.Status.OK.getStatusCode(), response.getStatus());
			assertNotNull("Should return song list as json", response.getEntity().toString());
			
			System.out.println(response.getStatus());
			System.out.println(response.readEntity(String.class));
    	}
	    
	    @Test
		public void getSong_Exists_Test() {
	    	
			Response response = target("/songs/1").request().get();
			assertEquals("Should return status 200", Response.Status.OK.getStatusCode(), response.getStatus());
			assertNotNull("Should return song object as json", response.getEntity());
			
			System.out.println(response.getStatus());
			System.out.println(response.readEntity(String.class));
		}
	    
	    @Test
		public void getSong_NotExists_Test() {
	    	
			Response response = target("/songs/1000").request().get();
			assertEquals("Should return status 404", Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
			
			System.out.println(response.getStatus());
		}
	    
	    
	    @Test
		public void createSong_BadMediaType_Test() {
	    	
	    	// Generate entity
	        Songs song = new Songs();
	        song.setTitle("testtitle");
	        song.setAlbum("testalbum");
	        song.setArtist("testartist");
	        song.setRelease(2000);
			
			Response response = target("/songs/").request().post(Entity.entity(song, MediaType.TEXT_HTML));
			assertEquals("Should return status 400", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
			
			System.out.println(response.getStatus());			
		}

		@Test
	    public void updateSong_Exists_Test() {
	        sDao = new SongsDBDAO(emf);

	        // Generate entity
	        Songs song = new Songs();
	        song.setTitle("testtitle");
	        song.setAlbum("testalbum");
	        song.setArtist("testartist");
	        song.setRelease(2000);

	        // send PUT request with JSON entity to REST path
	        Response response = target("songs/5").request().put(Entity.json(song));
	        assertEquals("Should return status 204", Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
	        
	        System.out.println(response.getStatus());
	        
	        Songs responseSong = sDao.getSong(5);
	        
	        assertNotNull(responseSong);
	        assertEquals(Integer.valueOf(5), responseSong.getId());
	        assertEquals("testtitle", responseSong.getTitle());
	        assertEquals("testalbum", responseSong.getArtist());
	        assertEquals("testartist", responseSong.getAlbum());
	        assertEquals(2000, responseSong.getRelease());
	    }
		
		 @Test
		 public void updateSong_NotExists_Test() {

			 // Generate entity
			 Songs song = new Songs();
			 song.setTitle("testtitle");
		     song.setAlbum("testalbum");
		     song.setArtist("testartist");
		     song.setRelease(2000);

		     Response response = target("songs/100").request().put(Entity.json(song));
		     assertEquals("Should return status 404", Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
		     
		     System.out.println(response.getStatus());
		 }
		 
		 @Test
		 public void deleteSong_Exists_Test() {
			  Response response = target("/songs/1").request().delete();
			  assertEquals("Should return status 204", Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
			  
			  System.out.println(response.getStatus());
		 }
		 
		 @Test
		 public void deleteSong_NotExists_Test() {
			  Response response = target("/songs/1000").request().delete();
			  assertEquals("Should return status 404", Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
			  
			  System.out.println(response.getStatus());
		 }
}
