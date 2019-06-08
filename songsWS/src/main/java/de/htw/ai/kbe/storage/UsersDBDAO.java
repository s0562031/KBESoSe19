package de.htw.ai.kbe.storage;


import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;


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
    
 

	@Override
	public Users getUserByID(String id) {
		
		EntityManager em = factory.createEntityManager();
		Users user = null;
	
		try {
            user = em.find(Users.class, id);

        } catch (Exception ex) {
        	
            ex.printStackTrace();
            em.getTransaction().rollback();
            
        } finally {
        	// EntityManager nach Datenbankaktionen wieder freigeben
            em.close();

        }
		
		return user;
	}
	
//	@Override
//	public boolean validateUser(String id, String pw) {
//		
//		List<Users> allusers = getAllUsers();
//		
//		
//	}
	
	@Override
	public boolean validateUser(String id, String pw) {
		
		EntityManager em = factory.createEntityManager();
		Users user = null;
		List<Users> userlist = null;
		
		try {
            em.getTransaction().begin();
            
            Query q = em.createQuery("SELECT u FROM userlist u WHERE u.userid = '" + id + "' AND u.password = '" + pw + "'", Users.class);
            
//            Query q = em.createQuery("SELECT * FROM userlist u WHERE u.userid = :userid AND u.password = :password", Users.class)
//            		.setParameter("userid", id)
//            		.setParameter("password", pw); 
            
            user =  (Users) q.getSingleResult();
            
            em.getTransaction().commit();

        } catch (NoResultException e) {
        	System.out.println("no result exception");
        	e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            // EntityManager nach Datenbankaktionen wieder freigeben
            em.close();
            // Freigabe am Ende der Applikation
            //factory.close();
        }
		
		System.out.println(user.toString());
		
		if(user != null && user.getUserId() == id && user.getPassword() == pw) return true;
		else return false;

		
		
	}

	@Override
	public List<Users> getAllUsers() {
		
		EntityManager em = factory.createEntityManager();		
		List<Users> userlist = null;
		
		try {
            em.getTransaction().begin();
            
            Query q = em.createQuery("SELECT u FROM userlist u", Users.class); //TODO naming of table?
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
