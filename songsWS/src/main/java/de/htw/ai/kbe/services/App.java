package de.htw.ai.kbe.services;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.xml.bind.JAXBException;
import de.htw.ai.kbe.bean.SongList;
import de.htw.ai.kbe.bean.Songs;
import de.htw.ai.kbe.storage.InMemorySongsDB;

public class App {
	
//	private String songsxmlfile = null;
//	private static List<Songs> songList = null;
//	private static SongList sl = new SongList();
	

    // entspricht <persistence-unit name="songDB-PU" transaction-type="RESOURCE_LOCAL"> in persistence.xml
//    private static final String PERSISTENCE_UNIT_NAME = "songDB-PU";
    
//    private static InMemorySongsDB inmemsongs = InMemorySongsDB.getInstance();

    //TODO EXCEPTIONS
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws FileNotFoundException, JAXBException, IOException, URISyntaxException {
    	
    	
    	DBWhisperer dbw = new DBWhisperer();
    	
    	System.out.println(dbw.getAllSongs());
    	System.out.println(dbw.getSongByID(2));
    	
    	
    	
    	
    	
//        // Datei persistence.xml wird automatisch eingelesen, 
//        // EntityManagerFactory wird nur einmal beim Start der Applikation erstellt
//        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
//
//        // EntityManager bietet Zugriff auf Datenbank
//        // EM wird bei jedem Zugriff auf die DB erstellt
//        EntityManager em = factory.createEntityManager();
//        
//         
//        for(Songs entry : inmemsongs.getAllSongs()) {
//        	System.out.println(entry.getTitle());
//        	
//        }
//        //System.out.println("Listsize:" + songList.size() + songList.get(2));
////        for(int i = 0; i<songList.size(); i++) {
////        	System.out.println(songList.get(i).getTitle());
////        }
//
//        try {
//            em.getTransaction().begin();
//            
//            for(Songs s : inmemsongs.getAllSongs()) {
//            	em.persist(s);
//            }
//            
//            int id = 0;
//            Query q = em.createQuery("SELECT u FROM Songs u");
//            List<Songs> loadedSongList = q.getResultList();
//            
//            System.out.println("All songs - size: " + loadedSongList.size());
//            
//            for (Songs u : loadedSongList) {
//                System.out.println(" with title: " + u.getTitle());
//            }
//
//            
//            // commit transaction
//            em.getTransaction().commit();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            em.getTransaction().rollback();
//        } finally {
//            // EntityManager nach Datenbankaktionen wieder freigeben
//            em.close();
//            // Freigabe am Ende der Applikation
//            factory.close();
//        }
    }
    
    
}