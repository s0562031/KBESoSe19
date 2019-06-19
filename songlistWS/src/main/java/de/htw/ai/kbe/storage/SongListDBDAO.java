package de.htw.ai.kbe.storage;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import de.htw.ai.kbe.data.SongList;
import de.htw.ai.kbe.data.Userlist;

public class SongListDBDAO implements ISongListDAO{
	
    private EntityManagerFactory factory;

    @Inject
    public SongListDBDAO(EntityManagerFactory emf) {
        this.factory = emf;
    }

    @Override
	public List<SongList> getAllOwnedSongLists(String owner) {
		
		EntityManager em = factory.createEntityManager();	
		List<SongList> foundsonglists = null;
		
		try {
            em.getTransaction().begin();
            
            Query q = em.createQuery("SELECT s FROM SongList s WHERE s.owner = '" + owner + "'", SongList.class);
            foundsonglists = q.getResultList();
            em.getTransaction().commit();

        } catch (NoResultException e) {
        	System.out.println("no result exception");
        	e.printStackTrace();
        	em.getTransaction().rollback();
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            // EntityManager nach Datenbankaktionen wieder freigeben
            em.close();
            // Freigabe am Ende der Applikation
            //factory.close();
        }
		
		return foundsonglists;
	}

	@Override
	public List<SongList> getAllForeignSongLists(String owner) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUserFromToken(String token) {
		
		EntityManager em = factory.createEntityManager();	
		String founduser = null;
		
		try {
            em.getTransaction().begin();
            
            Query q = em.createQuery("SELECT userid FROM Userlist u WHERE u.token = '" + token + "'", String.class);
            founduser = (String) q.getSingleResult();
            em.getTransaction().commit();

        } catch (NoResultException e) {
        	System.out.println("no result exception");
        	e.printStackTrace();
        	em.getTransaction().rollback();
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            // EntityManager nach Datenbankaktionen wieder freigeben
            em.close();
            // Freigabe am Ende der Applikation
            //factory.close();
        }
		
		return founduser;
	}

}
