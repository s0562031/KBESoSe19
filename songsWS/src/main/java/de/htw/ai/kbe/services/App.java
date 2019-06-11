package de.htw.ai.kbe.services;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.xml.bind.JAXBException;

import de.htw.ai.kbe.data.Songs;
import de.htw.ai.kbe.data.Userlist;
import de.htw.ai.kbe.storage.InMemorySongsDAO;

public class App {
		

    // entspricht <persistence-unit name="songDB-PU" transaction-type="RESOURCE_LOCAL"> in persistence.xml
    private static final String PERSISTENCE_UNIT_NAME = "songDB-PU";
    
    private static InMemorySongsDAO inmemsongs = InMemorySongsDAO.getInstance();

    //TODO EXCEPTIONS
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws FileNotFoundException, JAXBException, IOException, URISyntaxException {
    	
    	
//		SongsDBDAO dbw = SongsDBDAO;	
//    	System.out.println(dbw.getAllSongs());
//    	System.out.println(dbw.getSongByID(2));	    	
    	
    	
        // Datei persistence.xml wird automatisch eingelesen, 
        // EntityManagerFactory wird nur einmal beim Start der Applikation erstellt
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

        // EntityManager bietet Zugriff auf Datenbank
        // EM wird bei jedem Zugriff auf die DB erstellt
        EntityManager em = factory.createEntityManager();
        
         
        for(Songs entry : inmemsongs.getAllSongsAsCollection()) {
        	System.out.println(entry.getTitle());
        	
        }
        
//        try {
//            em.getTransaction().begin();
//            
//            em.createQuery("DELETE FROM Songs");
//            em.createQuery("DELETE FROM Userlist");            
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
//            //factory.close();
//        }
//        
//        em = factory.createEntityManager();


        try {
            em.getTransaction().begin();
                       
            
            for(Songs s : inmemsongs.getAllSongsAsCollection()) {
            	em.persist(s);
            }
            
            for(Userlist u : inmemsongs.getAllUsers()) {
            	em.persist(u);
            }

            // commit transaction
            em.getTransaction().commit();

        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            // EntityManager nach Datenbankaktionen wieder freigeben
            em.close();
            // Freigabe am Ende der Applikation
            factory.close();
        }
        
    }
    
  
}