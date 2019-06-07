package de.htw.ai.kbe.storage;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import de.htw.ai.kbe.data.Songs;
//import de.htw.ai.kbe.services.TokenGenerator;

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
            
            TypedQuery<Songs> q = em.createQuery("SELECT s FROM Songs s", Songs.class);
            loadedSongList = q.getResultList();
            
            em.getTransaction().commit();

        } catch (Exception ex) {
        	
            ex.printStackTrace();
            em.getTransaction().rollback();
            
        } finally {
        	
            // EntityManager nach Datenbankaktionen wieder freigeben
            em.close();
        }
		
		return loadedSongList;
	}
	
	
public Songs getSongByID(int id) {
		
        EntityManager em = factory.createEntityManager();
		Songs song = null;
	
		try {
            song = em.find(Songs.class, id);

        } catch (Exception ex) {
        	
            ex.printStackTrace();
            em.getTransaction().rollback();
            
        } finally {
        	// EntityManager nach Datenbankaktionen wieder freigeben
            em.close();

        }
		
		return song;
	}

	@Override
	public Songs getSong(Integer id) {
		
		EntityManager em = factory.createEntityManager();
		Songs song = null;
	
		try {
            song = em.find(Songs.class, id);

        } catch (Exception ex) {
        	
            ex.printStackTrace();
            em.getTransaction().rollback();
            
        } finally {
        	
        	// EntityManager nach Datenbankaktionen wieder freigeben
            em.close();
        }
		
		return song;
	}

	@Override
	public Integer addSong(Songs newsong) throws PersistenceException {
		
		EntityManager em = factory.createEntityManager();	
		
		try {
			em.getTransaction().begin(); 
            em.persist(newsong);
            em.getTransaction().commit();
            return newsong.getId();
           
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
            throw new PersistenceException("Error persisting entity: " + ex.toString());
            
        } finally {
        	
        	// EntityManager nach Datenbankaktionen wieder freigeben
            em.close();
        }
	}

	@Override
	public boolean updateSong(Songs updatesong, Integer id) throws PersistenceException {
		
		EntityManager em = factory.createEntityManager();	
		boolean execUpdt = false;
		
		if (updatesong != null){
			try {
		
				Songs song;
				
				if ((song = em.find(Songs.class, id)) != null) {
		            em.getTransaction().begin();
		            song.setTitle(updatesong.getTitle());
		            song.setArtist(updatesong.getArtist());
		            song.setAlbum(updatesong.getAlbum());
		            song.setRelease(updatesong.getRelease());                                 
		            em.getTransaction().commit();
	            
		            execUpdt = true; 
				}
	
	        } catch (Exception ex) {
	        	
	            ex.printStackTrace();
	            em.getTransaction().rollback();
	            throw new PersistenceException("Could not persist entity: " + ex.toString());
	            
	        } finally {
	        	
	        	// EntityManager nach Datenbankaktionen wieder freigeben
	            em.close();
	
	        }
		}
		return execUpdt;
	}

	@Override
	public boolean deleteSong(int id) {
		
		EntityManager em = factory.createEntityManager();			
		boolean delSong = false;
		
		try {
			Songs song;			
			song = em.find(Songs.class, id);
			
            if (song != null) {
				
            	em.getTransaction().begin();
	            em.remove(song);
	            em.getTransaction().commit();
	            delSong = true;
            }

        } catch (Exception ex) {
        	
            ex.printStackTrace();
            em.getTransaction().rollback();
            
        } finally {
            // EntityManager nach Datenbankaktionen wieder freigeben
            em.close();
        }
		
		return delSong;
	}
		
	public void destroy() {
		
		factory.close();
	}

}
