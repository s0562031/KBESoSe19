package de.htw.ai.kbe.songsServlet;

import java.io.BufferedInputStream;
import java.io.File;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;

/**
 * Einstellung fuer tomcat users
 * <user username="tomcat" password="tomcat" roles="manager-gui,manager-script"/>
 *
 *deploy: clean install tomcat7:deploy
 */

/**
 * 
 * @author dustin
 *
 */
public class App extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String songsxmlfile = null;
	private List<Songs> songList = null;
	private int currentSongId;
	private int nextSongId;
	private ObjectMapper objectmapper;
	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		
		objectmapper = new ObjectMapper();
	    
		// path declared in web.xml ??
		songsxmlfile = servletConfig.getInitParameter("songsxml");
				
		try {
			
			loadSongs(songsxmlfile);
			this.currentSongId = songList.size();
			this.nextSongId = this.currentSongId+1;
			
		} catch (JAXBException | IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
		
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String contentFormat = "application/json";
		String header = request.getHeader("Accept");
		
		if (request.getHeader("Accept") != null){
            header = request.getHeader("Accept");
        }
		
		/* HEADER ist irgendwas anderes 
		if (header != null && header != "application/json") {
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Header wird nicht akzeptiert");
			return;
        }*/
		
		/* just for testing
		 response.setContentType("text/plain");
		 

        try (PrintWriter te = response.getWriter()) {
            te.println(songList.size());
            te.println(header);
            te.close();
        } */ 
		
		
		// alle Parameter (keys)
		Enumeration<String> paramNames = request.getParameterNames();
		
		if (!paramNames.hasMoreElements()) {
			
			String allSongs = pojoToJSON(songList);			
			outputText(response, allSongs, contentFormat, "ok");
			
			return;
			
		} else {
			
			String param = paramNames.nextElement();
			
			if (songList == null){			
				response.sendError(HttpServletResponse.SC_NO_CONTENT, "Keine Daten vorhanden");
				return;
			}	
			
			switch (param) {
				
				case "songId": {
					
					try {
						 int id = Integer.parseInt(request.getParameter("songId")); // get Id from params				
					
						 Songs searchedSong = findSongById(songList, id); // get searchedSong	
						 
						 if(searchedSong == null) {
							 
							 response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Kein Song zu dieser songId");
							 return;
							 
						 } else {
							 
							 String song = pojoToJSON(searchedSong); //map searchedSong to JSON						
							 outputText(response, song, contentFormat, "ok");
						 }
						 
					} catch (NumberFormatException e) {				
						response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter ist keine Zahl");
					}
					
					return;
				}
				
				default: {
					
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter nicht erlaubt. Bitte songId angeben.");
					return;
				}			
			}		
		}
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String reqFormat = request.getContentType();
		
		if("application/json".contentEquals(reqFormat)) {
			
			Songs song = new Songs();
			getSong(request.getInputStream());
			
			synchronized (this) {
	            addSongToSongList(song);
	            outputText(response, "song mit id " + this.currentSongId + " erstellt.", "text/plain", "created");
	        }
			
		 } else {
			
			 response.sendError(HttpServletResponse.SC_BAD_REQUEST, "nur application/json als content type erlaubt");
             return;
		 }		
	}
	
	private void outputText(HttpServletResponse response, String content, String contentFormat, String status) throws IOException {
       
		response.setContentType(contentFormat);        
        response.setCharacterEncoding("UTF-8");
        
        if("created".equals(status)) {
        	response.setStatus(HttpServletResponse.SC_CREATED);
        } else {        	
        	response.setStatus(HttpServletResponse.SC_OK);
        }
        
        try (PrintWriter out = response.getWriter()) {
            out.println(content);
            out.close();
        }
    }
	
	private static Songs findSongById(List<Songs> songs, int id) {
        for (Songs song : songs) {
            if (song.getId().equals(id)) {
                return song;
            }
        }
        return null;
    }
	
	protected void loadSongs(String songsxmlfile) throws FileNotFoundException, JAXBException, IOException, URISyntaxException {
		
	       this.songList = readXMLToSongs(songsxmlfile);
	}
	
    private static List<Songs> readXMLToSongs(String filename) throws JAXBException, FileNotFoundException, IOException, URISyntaxException {
    	
        JAXBContext context = JAXBContext.newInstance(SongList.class, Songs.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        
        //String fn = App.class.getClassLoader().getResource(filename).toURI().getPath();
        
        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            List<Songs> songs = unmarshal(unmarshaller, Songs.class, filename);
            return songs;
        }
    }
    
    private static List<Songs> unmarshal(Unmarshaller unmarshaller, Class<Songs> clazz, String xmlLocation) throws JAXBException {
        
    	StreamSource xml = new StreamSource(xmlLocation);
        SongList wrapper = (SongList) unmarshaller.unmarshal(xml, SongList.class).getValue();
        return wrapper.getSongs();
    }
    
    private static String pojoToJSON(Object obj) throws JsonProcessingException {
    	
    	ObjectWriter mapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
    	return mapper.writeValueAsString(obj);
    }
    
    private static Songs getSong(InputStream stream) throws IOException {
    	
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(stream, Songs.class);
    }
    
    private void addSongToSongList(Songs song) {
    	
        song.setId(nextSongId);
        songList.add(song);       
        this.nextSongId++;              
    }
    
    
    @Override
    public void destroy() {
        try {
            objectmapper.writeValue(new File(songsxmlfile), songList);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
}
