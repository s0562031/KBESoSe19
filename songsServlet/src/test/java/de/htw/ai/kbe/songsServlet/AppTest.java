package de.htw.ai.kbe.songsServlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Unit test for simple SimpleServlet.
 */

// TODO: momentan testen wir nur die Methoden der SongsHelper in Verbindung mit den HTTP-Requests des Servlets, die Methoden an sich sind damit von den Tests nicht abgedeckt

public class AppTest {

    private App servlet;
    private MockServletConfig config;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private JSONObject songNine = null;
    private JSONObject mynewsong = null;

    @Before
    public void setUp() throws ServletException, JSONException {
        servlet = new App();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        response.setCharacterEncoding("UTF-8"); // very important, otherwise the strings can't be equal
        config = new MockServletConfig();
        
        File songfile = new File("src/test/resources/songs.xml");
        System.out.println(songfile.getAbsolutePath());
        
        config.addInitParameter("songsxml", songfile.getAbsolutePath());
        servlet.init(config); //throws ServletException
        
        
        songNine = new JSONObject();
        songNine.put("id", 9);
        songNine.put("title", "Private Show");
        songNine.put("artist", "Britney Spears");
        songNine.put("album", "Glory");
        songNine.put("released", 2016);
        
        mynewsong = new JSONObject();
        mynewsong.put("id", 50);
        mynewsong.put("title", "Wonderwall");
        mynewsong.put("artist", "Oasis");
        mynewsong.put("album", "Morning Glory");
        mynewsong.put("released", 1995);

    }
    
    @Before
    public void testXMLIntegrity() throws FileNotFoundException, JAXBException, IOException, URISyntaxException {
    	try {
    		servlet.loadSongs("src/test/resources/songs.xml");
    	} catch(Exception e) {
    		System.out.println("Corrupt XML, cannot perform further tests.");
    		System.exit(1);    
    	}
    }
    
    @Test
    public void initSongs() {
    	assertFalse(servlet.getSongList().isEmpty());
    }
    
    @Test
    public void getAllSongsGetsYouAllSongs() throws IOException {
    	request.addHeader("Accept", "application/json");
    	
    	//System.out.println(request.getHeader("Accept"));
    	
    	servlet.doGet(request, response);
    	
    	//System.out.println(response.getContentLength());
    	assertFalse(response.getContentAsString().isEmpty());
    	assert(response.getStatus() == 200);
    }
    
    @Test
    public void checkJSONIntegrityOf9() throws JSONException {
    	
    	JSONAssert.assertEquals("{id:9, title:\"Private Show\", artist:\"Britney Spears\", album:\"Glory\", released:2016}", songNine, false); 
    	JSONAssert.assertEquals("{id:9, title:\"Private Show\", artist:\"Britney Spears\", album:\"Glory\"}", songNine, false); 
    	JSONAssert.assertEquals("{id:9, title:\"Private Show\", artist:\"Britney Spears\"}", songNine, false); 
    	JSONAssert.assertEquals("{id:9, title:\"Private Show\"}", songNine, false); 
    	JSONAssert.assertEquals("{id:9}", songNine, false); 
    }
    
    
    @Test
    public void getJSONObjForSongID9() throws IOException {
    	request.addParameter("songId", "9");
    	request.addHeader("Accept", "application/json");
    	
    	/*
    	Enumeration<String> testenum = request.getParameterNames();
    	while(testenum.hasMoreElements()) {
    		System.out.println(testenum.nextElement());
    	}
    	*/
    	servlet.doGet(request, response);
    	
    	assertFalse(response.getContentAsString().isEmpty());
    	assertTrue(response.getContentAsString().contains("Private Show"));
    	assert(response.getStatus() == 200);
    }
    

    @Test
    public void doGetWithJSONInAcceptHeaderReturnsJSON() throws IOException {
        request.addHeader("Accept", "application/json");
        servlet.doGet(request, response);
        assertEquals(request.getHeader("Accept"), response.getContentType());
    }
    
    @Test
    public void doGetWithWOAcceptHeaderReturnsJSON() throws IOException {
        servlet.doGet(request, response);
        assertEquals("application/json", response.getContentType());
    }
    
    @Test
    public void doGetWithXMLInAcceptHeaderReturnsJSON() throws IOException {
    	request.addHeader("Accept", "application/xml");
        servlet.doGet(request, response);
        assert(response.getStatus() == 406);
    }
    
    
    @Test
    public void doGetWithWrongReturns400() throws IOException {
    	request.addParameter("songId", "thisisnotasongid");
    	request.addHeader("Accept", "application/json");
    	
    	servlet.doGet(request, response);
    	assert(response.getStatus() == 400);
    }
    
    /* TODO */
    
    @Test
    public void doGetWithMoreParamsReturns400() throws IOException {
    	request.addParameter("songId", "9");
    	request.addParameter("song", "10");
    	request.addHeader("Accept", "application/json");
    	
    	servlet.doGet(request, response);
    	//System.out.println(response.getStatus()); //200
    	assert(response.getStatus() == 400);
    }
    
    
    @Test
    public void doGetWithWrongParamsReturns400() throws IOException {
    	request.addParameter("thisisnotacorrectparam", "9");
    	request.addHeader("Accept", "application/json");
    	
    	servlet.doGet(request, response);
    	//System.out.println(response.getStatus()); //200
    	assert(response.getStatus() == 400);
    }
    
    @Test
    public void doGetWithoutValueParamsReturns400() throws IOException {
    	request.addParameter("songId", "");
    	request.addHeader("Accept", "application/json");
    	
    	servlet.doGet(request, response);
    	//System.out.println(response.getStatus()); //200
    	assert(response.getStatus() == 400);
    }
    
    @Test
    public void doGetWithoutNameParamsReturns400() throws IOException {
    	request.addParameter("", "9");
    	request.addHeader("Accept", "application/json");
    	
    	servlet.doGet(request, response);
    	//System.out.println(response.getStatus()); //200
    	assert(response.getStatus() == 400);
    }
    
    /* ID 50 */
    @Test
    public void postMyNewSongSongShouldGet201() throws IOException {
    	
    	request.setContentType("application/json");
    	request.setContent(mynewsong.toString().getBytes());
    	
    	servlet.doPost(request, response);    	
    	assert(response.getStatus() == 201);    	
    }
    
    @Test
    public void postXMLPayloadReturns400() throws IOException {
    	
    	request.setContentType("application/xml");
    	request.setContent(mynewsong.toString().getBytes());
    	
    	servlet.doPost(request, response);    	
    	assert(response.getStatus() == 400);    	
    }
    
    @Test
    public void postWOContentTypeReturns400() throws IOException {
    	
    	request.setContent(mynewsong.toString().getBytes());
    	
    	servlet.doPost(request, response);    	
    	//System.out.println(response.getStatus());
    	assert(response.getStatus() == 400);    	
    }

}

   