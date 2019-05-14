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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
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
	
	public static void main(String[] args) throws JAXBException, FileNotFoundException, IOException, URISyntaxException {
		
		App app = new App();
		app.test();
	}
	
	public void test() throws JAXBException, FileNotFoundException, IOException, URISyntaxException {
	
		loadSongs("/var/tmp/songs.xml");
		this.writeSongListToXML("/var/tmp/songs.xml");
	}
	
	
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
		String acceptheader = null;
				
		if(request.getHeader("Accept") != null) {
			acceptheader = request.getHeader("Accept");
			System.out.println("header: " + acceptheader.toString());
		} else {
			// wrap it to give it a header
	        HttpServletRequest req = (HttpServletRequest) request;
	        HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(req);
	        requestWrapper.addHeader("Accept", "application/json");
	        System.out.println("new header " + requestWrapper.getHeader("Accept").toString());
	        
	        acceptheader = requestWrapper.getHeader("Accept");
		}
		
		if (request.getContentType() != null && "application/json".contentEquals(request.getContentType())) {
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Header wird nicht akzeptiert");
			return;
        }
		
		if ("application/xml".contentEquals(acceptheader)) {
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Header wird nicht akzeptiert");
			return;
        }
		
		// alle Parameter (keys)
		Enumeration<String> paramNames = request.getParameterNames();
		
		if (!paramNames.hasMoreElements()) {
			
			String allSongs = pojoToJSON(songList);			
			outputText(response, allSongs, "application/json", "ok");
			
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
				
		// added check for null
		if(request.getContentType() != null && "application/json".contentEquals(request.getContentType())) {
			
			synchronized (this) {
				try {
					int currentSongId = nextSongId;
					writeNewJSONObjToSongList(request);
					
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
	
	/**
	 * Creates proper output for response.
	 * 
	 * @param response
	 * @param content
	 * @param contentFormat
	 * @param status
	 * @throws IOException
	 */
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
	
	/**
	 * Writes output from readXMLToSongs to songList.
	 * 
	 * @param songsxmlfile - path and name of xml
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	protected void loadSongs(String songsxmlfile) throws FileNotFoundException, JAXBException, IOException, URISyntaxException {
		
	       this.songList = readXMLToSongs(songsxmlfile);
	}
	
	/**
	 * Calls unmarshal to read songs from xml file.
	 * 
	 * @param filename - path of xml file
	 * @return
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
    private static List<Songs> readXMLToSongs(String filename) throws JAXBException, FileNotFoundException, IOException, URISyntaxException {
    	
        JAXBContext context = JAXBContext.newInstance(SongList.class, Songs.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        
        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            List<Songs> songs = unmarshal(unmarshaller, Songs.class, filename);
                     
            return songs;
        }
    }
    
    /**
     * Maps Songlist as a wrapper with XML annotation.
     * 
     * @param unmarshaller
     * @param clazz
     * @param xmlLocation
     * @return
     * @throws JAXBException
     */
    private static List<Songs> unmarshal(Unmarshaller unmarshaller, Class<Songs> clazz, String xmlLocation) throws JAXBException {
        
    	StreamSource xml = new StreamSource(xmlLocation);
        SongList wrapper = (SongList) unmarshaller.unmarshal(xml, SongList.class).getValue();
        return wrapper.getSongs();
    }
    
    /**
     * Uses SongList as a wrapper to map POJOS of Songs in List<Songs> back to xml.
     * 
     * @param filename
     * @throws JAXBException
     * @throws FileNotFoundException 
     */
    private void writeSongListToXML(String filename) throws JAXBException, FileNotFoundException {
    	
    	//System.out.println("filename:" + filename);
    	
    	JAXBContext context = JAXBContext.newInstance(SongList.class, Songs.class);
    	Marshaller marshall = context.createMarshaller();
    	marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    	SongList wrapper = new SongList(songList);    	
    	marshall.marshal(wrapper, new FileOutputStream(filename));
    	
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
    
//    private static String JSONToPojo(Object obj) throws JsonProcessingException {
//    	
//    	ObjectWriter mapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
//    	return mapper.writeValueAsString(obj);
//    }
//    
//    private static Songs getSong(InputStream stream) throws IOException {
//    	
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.readValue(stream, Songs.class);
//    }
    
    /**
     * Maps JSON object to song POJO and calls addSongToSongList.
     * 
     * @param request
     * @throws IOException
     */
    private void writeNewJSONObjToSongList(HttpServletRequest request) throws IOException {
    	
		ServletInputStream inputStream = request.getInputStream();
	    Map<String, Object> jsonMap =  this.objectmapper.readValue(inputStream, new TypeReference<Map<String, Object>>(){});
	       
	    Songs newSong = objectmapper.convertValue(jsonMap, new TypeReference<Songs>() {});
	    
	    this.addSongToSongList(newSong);
    }
    
    /**
     * Adds songs to SongList and sorts it by id.
     * 
     * @param song
     */
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
    
    /**
     * Returns songList.
     * 
     * @return
     */
    public List<Songs> getSongList() {
    	return songList;
    }
     
    /**
     * Writes all songs in songList to xml given in web.xml config file.
     * 
     */
    @Override
    public void destroy() {
        try {
        	if(songList != null) {
        		this.writeSongListToXML(songsxmlfile);
        	}       	
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    /* ############################################################################################################# */
    
    /**
     * Inner Class to wrap requests and override header
     * found on: https://stackoverflow.com/questions/2811769/adding-an-http-header-to-the-request-in-a-servlet-filter
     * answer by Wolfgang Fahl
     * @author @git miku
     *
     */
    
    public class HeaderMapRequestWrapper extends HttpServletRequestWrapper {
        /**
         * construct a wrapper for this request
         * 
         * @param request
         */
        public HeaderMapRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        private Map<String, String> headerMap = new HashMap<String, String>();

        /**
         * add a header with given name and value
         * 
         * @param name
         * @param value
         */
        public void addHeader(String name, String value) {
            headerMap.put(name, value);
        }

        @Override
        public String getHeader(String name) {
            String headerValue = super.getHeader(name);
            if (headerMap.containsKey(name)) {
                headerValue = headerMap.get(name);
            }
            return headerValue;
        }

        /**
         * get the Header names
         */
        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> names = Collections.list(super.getHeaderNames());
            for (String name : headerMap.keySet()) {
                names.add(name);
            }
            return Collections.enumeration(names);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            List<String> values = Collections.list(super.getHeaders(name));
            if (headerMap.containsKey(name)) {
                values.add(headerMap.get(name));
            }
            return Collections.enumeration(values);
        }

    }
    
    
	
}


 