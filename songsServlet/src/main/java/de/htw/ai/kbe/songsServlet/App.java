package de.htw.ai.kbe.songsServlet;

import java.io.BufferedInputStream;
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



public class App extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String songsxmlfile = null;
	private List<Songs> songList = null;
	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
	    
		// path declared in web.xml ??
		this.songsxmlfile = servletConfig.getInitParameter("songsxml");
		loadSongs(songsxmlfile);
	}
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		// alle Parameter (keys)
		Enumeration<String> paramNames = request.getParameterNames();
	
		// check if any param exists, more than songId..
		// check f Accept-Header exists
		
		
		// get Id from params
		String responseStr = "this is your response";
		String id = request.getParameter("songId");
		
		String param = "";

		while (paramNames.hasMoreElements()) {
			param = paramNames.nextElement();
			responseStr = responseStr + param + "=" 
			+ request.getParameter(param) + "\n";
		}
		

		response.setContentType("text/plain");		
		try (PrintWriter out = response.getWriter()) {
			responseStr += "\n repsonse " + responseStr;
			out.println(responseStr);
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
	
	protected void loadSongs(String songsxmlfile) {
		
		 try {
	            List<Songs> readSongs = readXMLToSongs(songsxmlfile);
	            readSongs.forEach(s -> {
	                System.out.println(s.getTitle());
	            });
	        } catch (NullPointerException e) {
	            e.printStackTrace();  
	        } catch (FileNotFoundException e) {
	        	e.printStackTrace();
	        } catch (IOException e) {
	        	e.printStackTrace();
	        } catch (JAXBException e) {
	        	e.printStackTrace();
	        }
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

}
