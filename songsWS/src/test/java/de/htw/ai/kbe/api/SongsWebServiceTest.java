package de.htw.ai.kbe.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
//import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;


import de.htw.ai.kbe.data.Songs;
import de.htw.ai.kbe.storage.ISongsDAO;
import de.htw.ai.kbe.storage.IUsersDAO;
import de.htw.ai.kbe.storage.SongsDBDAO;
import de.htw.ai.kbe.storage.UsersDBDAO;


public class SongsWebServiceTest extends JerseyTest {
	
	    private String tk = "MzIxZHdzc2Fw";
	    private String jsonHeader = "application/json";
	    private String xmlHeader = "application/xml";
	    private String plainHeader = "text/plain";

	    @Override
	    public Application configure() {
	    	return new ResourceConfig(SongsWebService.class).register(new AbstractBinder() {
	            @Override
	            protected void configure() {
	            	bind(Persistence.createEntityManagerFactory("songDB-PU")).to(EntityManagerFactory.class);
	                bind(SongsDBDAO.class).to(ISongsDAO.class).in(Singleton.class);
	                bind(UsersDBDAO.class).to(IUsersDAO.class);
	            }
	        });
	    }
	    
	    @Test
		public void getAllSongs_OK_ShouldReturn200() {
	    	
	    	Response response = target("/songs/").request().header("Authorization", tk).header("Accept", jsonHeader).get();
			assertEquals("should return status 200", Response.Status.OK.getStatusCode(), response.getStatus());
			assertNotNull("Should return song list as json", response.getEntity().toString());
			
			//System.out.println(response.toString());
			//System.out.println(response.getStatus());
			//System.out.println(response.readEntity(String.class));
    	}
   
//	    @Test
//	    public void putSongShouldReturn204() {
//
//	        Songs song_two = new Songs.Builder("Test")
//	                .artist("Test")
//	                .album("Test")
//	                .release(2016).build();
//	        song_two.setId(10);
//	    	
//	    	Songs mynewsong = new Songs();
//	    	mynewsong.setAlbum("Testablum");
//	    	mynewsong.setId(10);
//	    	mynewsong.setArtist("Testartist");
//	    	mynewsong.setRelease(1000);
//	    	
//	    	Entity e = Entity.json(song_two);
//	    	System.out.println(e);
//	    	
//	    	System.out.println(song_two.getTitle());
//	    	
//	    	Response response = target("/songs").request().put(e);
//	    	System.out.println(response.toString());
//	    	Map<String,List<Object>> headermap = (response.getHeaders());
//	    	
//	    	System.out.println(headermap.get("Content-Type"));
//	    	System.out.println(headermap.get("Content-Type").get(0));
//	    	assertEquals(headermap.get("Content-Type").get(0), MediaType.APPLICATION_JSON);
//	    	
//	    	
//	    }

	    
	    /*@Test
	    public void getAllSongs_XML_OK_ShouldReturnXML() {
	        String response = target("/songs/").request().header("Authorization", tk).header("Accept", xmlHeader).get(String.class);       
	        //assertTrue(response.startsWith("<?xml"));
	        
	          System.out.println(response.toString());
	    }*/

	    
	    @Test
		public void getAllSongs_UnacceptableHeader_ShouldReturn406() {
	    	
	    	Response response = target("/songs/").request().header("Authorization", tk).header("Accept", plainHeader).get();
			assertEquals("should return status 406", Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
			
			//System.out.println(response.toString());
			//System.out.println(response.getStatus());
    	}
	    
	    @Test
		public void getAllSongs_NoAuthorisation_ShouldReturn404() {
	    	
	    	Response response = target("/songs/").request().header("Accept", jsonHeader).get();
			assertEquals("should return status 404", Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
			
			//System.out.println(response.toString());
			//System.out.println(response.getStatus());
    	}
	    
	    @Test
		public void getSong_Exists_ShouldReturn200() {
	    	
	    	Response response = target("/songs/1").request().header("Authorization", tk).header("Accept", jsonHeader).get();
			assertEquals("Should return status 200", Response.Status.OK.getStatusCode(), response.getStatus());
			assertNotNull("Should return song object as json", response.getEntity());
			
			//System.out.println(response.toString());
			//System.out.println(response.getStatus());
			//System.out.println(response.readEntity(String.class));
		}
	    
	    @Test
		public void getSong_NotExists_ShouldReturn404() {
	    	
	    	Response response = target("/songs/1000").request().header("Authorization", tk).header("Accept", jsonHeader).get();
			assertEquals("Should return status 404", Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
			
			//System.out.println(response.toString());
			//System.out.println(response.getStatus());
		}
	    
	    @Test
		public void createSong_JSON_ShouldReturn201AndId() {
	    	
	    	// Generate entity
	        Songs song = new Songs();
	        song.setTitle("testtitle");
	        song.setAlbum("testalbum");
	        song.setArtist("testartist");
	        song.setRelease(2000);
			
			Response response = target("/songs/").request().header("Authorization", tk).post(Entity.entity(song, MediaType.APPLICATION_JSON));
			assertEquals("Should return status 201", Response.Status.CREATED.getStatusCode(), response.getStatus());
			//assertTrue(response.getLocation().toString().endsWith("2"));
			//System.out.println(response.getStatus());			
		}
	    
	    @Test
		public void createSong_XML_ShouldReturn201AndId() {
	    	
	    	// Generate entity
	        Songs song = new Songs();
	        song.setTitle("testtitle");
	        song.setAlbum("testalbum");
	        song.setArtist("testartist");
	        song.setRelease(2000);
			
			Response response = target("/songs/").request().header("Authorization", tk).post(Entity.entity(song, MediaType.APPLICATION_XML));
			assertEquals("Should return status 201", Response.Status.CREATED.getStatusCode(), response.getStatus());
			//assertTrue(response.getLocation().toString().endsWith("3"));
			
			//System.out.println(response.getStatus());			
		}
	    
	    @Test
	    public void updateSong_Exists_ShouldReturn204() {
	        //sDao = new SongsDBDAO(emf);

	        // Generate entity
	        Songs song = new Songs();
	        song.setTitle("testtitle");
	        song.setAlbum("testalbum");
	        song.setArtist("testartist");
	        song.setRelease(2000);

	        // send PUT request with JSON entity to REST path
	        Response response = target("songs/1").request().header("Authorization", tk).put(Entity.json(song));
	        assertEquals("Should return status 204", Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
	        
	        //System.out.println(response.getStatus());

	    }
	    
	    @Test
		 public void updateSong_NotExists_ShouldReturn404() {

			 // Generate entity
			 Songs song = new Songs();
			 song.setTitle("testtitle");
		     song.setAlbum("testalbum");
		     song.setArtist("testartist");
		     song.setRelease(2000);

		     Response response = target("songs/100").request().header("Authorization", tk).put(Entity.json(song));
		     assertEquals("Should return status 404", Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
		     
		     //System.out.println(response.getStatus());
		 }
		
	     @Test
		 public void deleteSong_Exists_ShouldReturn204() {
			  Response response = target("/songs/3").request().header("Authorization", tk).delete();
			  assertEquals("Should return status 204", Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
			  
			  //System.out.println(response.getStatus());
		 }
		 
		 @Test
		 public void deleteSong_NotExists_ShouldReturn404() {
			  Response response = target("/songs/1000").request().header("Authorization", tk).delete();
			  assertEquals("Should return status 404", Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
			  
			  //System.out.println(response.getStatus());
		 }
	    
}
