package de.htw.ai.kbe.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import de.htw.ai.kbe.bean.Songs;

public class DBWhisperer {
	
	private static TokenGenerator tG = new TokenGenerator();
	private static EntityManager em = null;
	private static EntityManagerFactory factory = null;
	private static final String PERSISTENCE_UNIT_NAME = "songDB-PU";
	
	public DBWhisperer() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();	
	}
	
	public List<Songs> getAllSongs() {
		
		List<Songs> loadedSongList = null;
		
		try {
            em.getTransaction().begin();
            
            Query q = em.createQuery("SELECT s FROM Songs s");
            loadedSongList = q.getResultList();
            
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
		
		return loadedSongList;
	}
	
	public Songs getSongByID(int id) {
		
		List<Songs> loadedSongList = null;
	
		try {
            em.getTransaction().begin();
            
            Query q = em.createQuery("SELECT s FROM Songs s WHERE s.id = :id");
            loadedSongList = q.setParameter("id", id).getResultList();
                        
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
		
		return loadedSongList.get(0);
	}

}
