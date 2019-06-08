package de.htw.ai.kbe.storage;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;


import de.htw.ai.kbe.data.Userlist;
import de.htw.ai.kbe.services.TokenHandler;

@Singleton
public class UsersDBDAO implements IUsersDAO {
	
    private EntityManagerFactory factory;
    // bad should be DI
    private TokenHandler tk = new TokenHandler();

    @Inject
    public UsersDBDAO(EntityManagerFactory emf) {
        this.factory = emf;
    }
    
 

	@Override
	public Userlist getUserByID(String id) {
		
		EntityManager em = factory.createEntityManager();
		Userlist user = null;
	
		try {
            user = em.find(Userlist.class, id);

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
		Userlist user = null;
		Object result = null;
		List<Userlist> userlist = null;
		
		try {
            em.getTransaction().begin();
            
            Query q = em.createQuery("SELECT u FROM Userlist u WHERE u.userid = '" + id + "' AND u.password = '" + pw + "'", Userlist.class);
            
//            Query q = em.createQuery("SELECT * FROM userlist u WHERE u.userid = :userid AND u.password = :password", Users.class)
//            		.setParameter("userid", id)
//            		.setParameter("password", pw); 
            
         
            user =  (Userlist) q.getSingleResult();
            
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
		
		//System.out.println(user.toString());
		
		System.out.println("###################" + user.getPassword() + "#############");
		//if(result != null && result.toString().equals(pw)) return true;
		if(user != null && user.getUserId().contentEquals(id) && user.getPassword().contentEquals(pw)) return true;
		else return false;

		
		
	}

	@Override
	public List<Userlist> getAllUsers() {
		
		EntityManager em = factory.createEntityManager();		
		List<Userlist> userlist = null;
		
		try {
            em.getTransaction().begin();
            
            Query q = em.createQuery("SELECT u FROM Userlist u", Userlist.class); //TODO naming of table?
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
