package de.htw.ai.kbe.songsServlet;

import java.io.BufferedInputStream;
import java.io.File;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

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
	private int nextSongId;
	private ObjectMapper objectmapper;
	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		
		objectmapper = new ObjectMapper();
	    
		// path declared in web.xml ??
		songsxmlfile = servletConfig.getInitParameter("songsxml");
				
		try {
			
			loadSongs(songsxmlfile);
			this.nextSongId = songList.size()+1;
			
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
		
		if (request.getContentType() != null && "application/json".contentEquals(request.getContentType())) {
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, header);
			return;
        }
		
		if ("application/xml".contentEquals(header)) {
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, header);
			return;
        }
		
		if(request.getContentType() == null) {
			// ???
		}
		
		// alle Parameter (keys)
		Enumeration<String> paramNames = request.getParameterNames();
		
		if (!paramNames.hasMoreElements()) {
			
			String allSongs = pojoToJSON(songList);			
			outputText(response, allSongs, contentFormat, "ok");
			
			return;
			
		} else {
			
			List<String> pnames = new ArrayList<String>(request.getParameterMap().keySet());
			
			if(pnames.size() > 1) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nur ein Parameter möglich: songId");
				return;		 
			}
			
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
				
		// addes check for null
		if(request.getContentType() != null && "application/json".contentEquals(request.getContentType())) {
			
			synchronized (this) {
				try {
					int currentSongId = nextSongId;
					writeNewJSONObjToXML(request);
					
					response.setHeader("Location", "http://localhost:8080/songsServlet?songId="+currentSongId);
                    response.setStatus(201);
				}
				catch(IOException e) {
					e.printStackTrace();
					 response.sendError(HttpServletResponse.SC_BAD_REQUEST, "song konnte nicht geschrieben werden");
				}	            
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
    
    private static String JSONToPojo(Object obj) throws JsonProcessingException {
    	
    	ObjectWriter mapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
    	return mapper.writeValueAsString(obj);
    }
    
    private static Songs getSong(InputStream stream) throws IOException {
    	
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(stream, Songs.class);
    }
    
    private void writeNewJSONObjToXML(HttpServletRequest request) throws IOException {
    	
		ServletInputStream inputStream = request.getInputStream();
	    Map<String, Object> jsonMap =  this.objectmapper.readValue(inputStream, new TypeReference<Map<String, Object>>(){});
	       
	    Songs newSong = objectmapper.convertValue(jsonMap, new TypeReference<Songs>() {});
	    
	    this.addSongToSongList(newSong);
    }
    
    private void addSongToSongList(Songs song) {
    	
        song.setId(nextSongId);
        songList.add(song);   
        
        Collections.sort(songList, new Comparator<Songs>() {
            @Override
            public int compare(Songs o1, Songs o2) {
                return o1.getId() > o2.getId() ? -1 : (o1.getId() < o2.getId()) ? 1 : 0;
            }
        });
                
        this.nextSongId++;              
    }
    
    public List<Songs> getSongList() {
    	return songList;
    }
     
    @Override
    public void destroy() {
        try {
        	if(songList != null) {
        		//XmlMapper xmlMapper = new XmlMapper();
        		//xmlMapper.writerWithDefaultPrettyPrinter().writeValue(new File(songsxmlfile), songList);
        	}       	
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
}
 