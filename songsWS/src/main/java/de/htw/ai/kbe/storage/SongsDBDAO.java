package de.htw.ai.kbe.storage;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import de.htw.ai.kbe.data.Songs;
import de.htw.ai.kbe.services.TokenGenerator;

@Singleton
public class SongsDBDAO implements ISongsDAO {
	
	//private static TokenGenerator tG = new TokenGenerator();
	//private static EntityManager em = null;
	//private static EntityManagerFactory factory = null;
	//private static final String PERSISTENCE_UNIT_NAME = "songDB-PU";
	
    private EntityManagerFactory factory;

    @Inject
    public SongsDBDAO(EntityManagerFactory emf) {
        this.factory = emf;
    }
	
	public List<Songs> getAllSongs() {
		
        EntityManager em = factory.createEntityManager();		
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
            //factory.close();
        }
		
		return loadedSongList;
	}
	
	public Songs getSongByID(int id) {
		
        EntityManager em = factory.createEntityManager();
		//List<Songs> loadedSongList = null;
		Songs song = null;
	
		try {
            //em.getTransaction().begin();
            
            //Query q = em.createQuery("SELECT s FROM Songs s WHERE s.id = :id");
            //loadedSongList = q.setParameter("id", id).getResultList();
                        
            //em.getTransaction().commit();
			
			song = em.find(Songs.class, id);

        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            // EntityManager nach Datenbankaktionen wieder freigeben
            em.close();
            // Freigabe am Ende der Applikation
            //factory.close();
        }
		
		return song;
	}

	@Override
	public Songs getSong(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer addSong(Songs newsong) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateSong(Songs updatesong) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Songs deleteSong(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

}
