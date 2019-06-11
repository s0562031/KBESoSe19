package de.htw.ai.kbe.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

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
import org.glassfish.jersey.test.TestProperties;
import org.json.simple.JSONObject;
import org.junit.Test;


import de.htw.ai.kbe.data.Songs;
import de.htw.ai.kbe.storage.ISongsDAO;
import de.htw.ai.kbe.storage.InMemorySongsDAO;
import de.htw.ai.kbe.storage.InMemorySongsDB;

import org.junit.Assert;
import org.junit.Before;
//import org.json.JSONObject;



public class SongsWebServiceTest extends JerseyTest {
	
	 	//SongsDBDAO sDao;	 
	    //private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("songDB-PU");
		//private Songs mynewsong = null;

	

	    @Override
	    public Application configure() {
	    	return new ResourceConfig(SongsWebService.class).register(new AbstractBinder() {
	            @Override
	            protected void configure() {
	                bind(InMemorySongsDAO.class).to(ISongsDAO.class).in(Singleton.class);
	            }
	        });
	    }
	    
	    
	    @Before
	    public void setUp() {
	    	
//	    	mynewsong = new Songs();
//	    	mynewsong.setAlbum("Testablum");
//	    	mynewsong.setId(10);
//	    	mynewsong.setArtist("Testartist");
//	    	mynewsong.setRelease(1000);

	    }
	    
	    @Test
	    public void getSongOne() {
	    	
	    	Response resp = target("/songs/1").request().get();
	    	System.out.println(resp.toString());
	    }
//	    
//	    
//	    
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
	    
	    
	 	
	    /*
	 	@Override
		public Application configure() {
			enable(TestProperties.LOG_TRAFFIC);
			enable(TestProperties.DUMP_ENTITY);
			return new ResourceConfig(SongsWebService.class);
		}
		*/
	 	/*
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
			assertEquals("Should return status 415", Response.Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode(), response.getStatus());
			
			System.out.println(response.getStatus());			
		}

		@Test
	    public void updateSong_Exists_Test() {
	        //sDao = new SongsDBDAO(emf);

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
		 } */
}
