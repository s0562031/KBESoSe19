package de.htw.ai.kbe.songsServlet;

import java.io.BufferedInputStream;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletConfig;
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
	private static String songsxmlfile = null;
	private static List<Songs> songList = null;
	
	public static void main(String[] args) {
		
		/*
		songsxmlfile = "/var/tmp/songs.xml";
		
		try {
			loadSongs(songsxmlfile);
		} catch (JAXBException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		songList.forEach(s -> {
			System.out.println(s.getTitle());
		});

		*/
		
		try {
            List<Songs> readSongs = readXMLToSongs("/var/tmp/songs.xml");
            readSongs.forEach(s -> {
                System.out.println(s.getTitle());
            });
        } catch (Exception e) { // Was stimmt hier nicht?
            e.printStackTrace();  
        }
        
	}
	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
	    
		// path declared in web.xml ??
		this.songsxmlfile = servletConfig.getInitParameter("songsxml");
		System.out.println(songsxmlfile);
		
		try {
			loadSongs(songsxmlfile);
		} catch (JAXBException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			songList = readXMLToSongs("songs.xml");
			
		} catch(Exception e) { //TODO: define
			e.printStackTrace();
		}
		
		songList.forEach(s -> {
			System.out.println(s.getTitle());
		});

	}
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		// alle Parameter (keys)
		Enumeration<String> paramNames = request.getParameterNames();
		System.out.println(paramNames.toString());
	
		// check if any param exists, more than songId..
		// check f Accept-Header exists
		
		
		// get Id from params
		int id = Integer.parseInt(request.getParameter("songId"));
		
		// get searchedSong
		Songs searchedSong = songList.get(id);
		
		//map searchedSong to JSON
		
		String songJSONObj = songToJSON(searchedSong);
		
		System.out.println(songJSONObj);

		
		response.setContentType("application/json");		
		response.setCharacterEncoding("UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.println(songJSONObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
//		response.setContentType("text/plain");
//		ServletInputStream inputStream = request.getInputStream();
//		byte[] inBytes = IOUtils.toByteArray(inputStream);
//		
//		try (PrintWriter out = response.getWriter()) {
//			out.println(new String(inBytes));
//		}
	}
	
	protected static void loadSongs(String songsxmlfile) throws FileNotFoundException, JAXBException, IOException {
		
	          songList = readXMLToSongs(songsxmlfile);
	}
	
    private static List<Songs> readXMLToSongs(String filename) throws JAXBException, FileNotFoundException, IOException {
    	
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
    
    private static String songToJSON(Songs searchedSong) throws JsonProcessingException {
    	
    	ObjectWriter mapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
    	
    	String json = mapper.writeValueAsString(searchedSong);
    	return json;
    }

}
