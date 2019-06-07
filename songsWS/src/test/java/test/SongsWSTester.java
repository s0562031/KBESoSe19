//package test;
//
//import java.util.List;
//
//import javax.ws.rs.Path;
//import javax.ws.rs.core.Application;
//import javax.ws.rs.core.MediaType;
//
//import org.glassfish.jersey.server.ResourceConfig;
//import org.glassfish.jersey.test.JerseyTest;
////import org.junit.jupiter.api.Test;
//
//import de.htw.ai.kbe.services.*;
//import de.htw.ai.kbe.bean.*;
//import de.htw.ai.kbe.services.SongsWebService;
//
//public class SongsWSTester extends JerseyTest{
//	
//	@Override
//	protected Application configure() {
//		return new ResourceConfig(SongsWebService.class);
//	}
//	
////	@Test
////	public void getSongsDefaultContentTypeShouldBeJSON() {
////		String response = target("/songs/1").request().get(String.class);
////		System.out.println(response);
////	}
////	
////	@Test
////	public void getAllSongsAsXMLShouldReturnXML() {
////		List response = target("/songs").request(MediaType.APPLICATION_XML).get(List.class);
////		System.out.println(response);
////	}
//
//}
