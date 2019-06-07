package de.htw.ai.kbe.storage;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import de.htw.ai.kbe.data.Songs;
import de.htw.ai.kbe.data.Users;

public class UsersDBDAO implements IUsersDAO {
	
    private EntityManagerFactory factory;

    @Inject
    public UsersDBDAO(EntityManagerFactory emf) {
        this.factory = emf;
    }

	@Override
	public Users getUserByID(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Users> getAllUsers() {
		
		EntityManager em = factory.createEntityManager();		
		List<Users> userlist = null;
		
		try {
            em.getTransaction().begin();
            
            Query q = em.createQuery("SELECT u FROM userlist u"); //TODO naming of table?
            userlist = q.getResultList();
            
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
		
		return userlist;
	}

	@Override
	public void saveUser() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCUser() {
		// TODO Auto-generated method stub
		
	}

}
