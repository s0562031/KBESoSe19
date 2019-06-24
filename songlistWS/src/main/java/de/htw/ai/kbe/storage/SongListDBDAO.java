package de.htw.ai.kbe.storage;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import de.htw.ai.kbe.data.SongList;
import de.htw.ai.kbe.data.Songs;
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
		
		for(int i=0; i<foundsonglists.size(); i++) {
			foundsonglists.get(i).getOwner().setPassword("");
			foundsonglists.get(i).getOwner().setToken("");
		}
		
		return foundsonglists;
	}

	@Override
	public List<SongList> getAllForeignSongLists(String owner) {
		
		EntityManager em = factory.createEntityManager();	
		List<SongList> foundsonglists = null;
		
		try {
            em.getTransaction().begin();
            
            Query q = em.createQuery("SELECT s FROM SongList s WHERE s.owner = '" + owner + "' AND s.isprivate = '" + false + "'", SongList.class);
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
		
		for(int i=0; i<foundsonglists.size(); i++) {
			foundsonglists.get(i).getOwner().setPassword("");
			foundsonglists.get(i).getOwner().setToken("");
		}
		
		return foundsonglists;
		
	}
	
	@Override
	public SongList getOwnedSongList(String owner, Integer songlistid) {
		
		EntityManager em = factory.createEntityManager();	
		SongList foundsonglist = null;
		
		try {
            em.getTransaction().begin();
            
            Query q = em.createQuery("SELECT s FROM SongList s WHERE s.owner = '" + owner + "' AND s.id = '" + songlistid + "'", SongList.class);
            foundsonglist = (SongList) q.getSingleResult();
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
		
		if(foundsonglist != null) {
			foundsonglist.getOwner().setPassword("");
			foundsonglist.getOwner().setToken("");
		}
		
		return foundsonglist;
	}

	@Override
	public SongList getForeignSongList(Integer songlistid) {
		
		EntityManager em = factory.createEntityManager();	
		SongList foundsonglist = null;
		
		try {
            em.getTransaction().begin();
            
            Query q = em.createQuery("SELECT s FROM SongList s WHERE s.id = '" + songlistid + "' AND s.isprivate ='" + false + "'", SongList.class);
            foundsonglist = (SongList) q.getSingleResult();
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
		
		if(foundsonglist != null) {
			foundsonglist.getOwner().setPassword("");
			foundsonglist.getOwner().setToken("");
		}

		
		return foundsonglist;
	}
	
	/**
	 * 
	 */
	@Override
	public void deleteSongList(Integer songlistid) {
		
		EntityManager em = factory.createEntityManager();	
		
		try {
            
            SongList songlist;			
            songlist = em.find(SongList.class, songlistid);
			
            if (songlist != null) {
				
            	em.getTransaction().begin();
	            em.remove(songlist);
	            em.getTransaction().commit();
            }

        } catch (NoResultException e) {
        	System.out.println("no result exception");
        	e.printStackTrace();
        	em.getTransaction().rollback();
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
	}
	
	/**
	 * 
	 */
	@Override
	public String getSongListOwner(Integer songlistid) {
		
		EntityManager em = factory.createEntityManager();	
		SongList foundsonglist = null;
		
		try {
            em.getTransaction().begin();
            
            Query q = em.createQuery("SELECT s FROM SongList s WHERE s.id = '" + songlistid + "'", SongList.class);
            foundsonglist = (SongList) q.getSingleResult();
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
		
		if(foundsonglist == null) return null;
		else return foundsonglist.getOwner().getUserid();
	}

	/**
	 * 
	 */
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
	
	@Override
	public Integer createSongList(SongList newsonglist) throws PersistenceException {
		
		EntityManager em = factory.createEntityManager();	
		
		try {
			em.getTransaction().begin(); 
            em.persist(newsonglist);
            em.getTransaction().commit();
            return newsonglist.getId();
           
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
            throw new PersistenceException("Error persisting entity: " + ex.toString());
            
        } finally {
        	
        	// EntityManager nach Datenbankaktionen wieder freigeben
            em.close();
        }
	}

}