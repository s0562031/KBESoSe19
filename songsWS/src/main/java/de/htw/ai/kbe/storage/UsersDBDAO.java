package de.htw.ai.kbe.storage;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import de.htw.ai.kbe.data.Songs;
import de.htw.ai.kbe.data.Users;
import de.htw.ai.kbe.services.TokenHandler;

public class UsersDBDAO implements IUsersDAO {
	
    private EntityManagerFactory factory;
    // bad should be DI
    private TokenHandler tk = new TokenHandler();

    @Inject
    public UsersDBDAO(EntityManagerFactory emf) {
        this.factory = emf;
    }
    
    public String getToken(String userid, String pw) {
    	
    	EntityManager em = factory.createEntityManager();		
		List<Users> userlist = null;
		
		Users user = this.getUserByID(Integer.parseInt(userid));
		
		// if user legit, get him a token
		if(user.getPassword() == pw) {
			
			try {
				tk.generateToken(Integer.parseInt(userid), pw);
			} catch (NumberFormatException | NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
    	
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
