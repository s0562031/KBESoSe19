package de.htw.ai.kbe.storage;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import de.htw.ai.kbe.data.Songs;
import de.htw.ai.kbe.data.Userlist;
import de.htw.ai.kbe.services.TokenHandler;

@Singleton
public class UsersDBDAO implements IUsersDAO {
	
    private EntityManagerFactory factory;
    //private TokenHandler th = TokenHandler.getInstance();

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
	
//	public boolean validateToken(String token) {
//		if(th.findToken(token)) return true;
//		else return false;
//	}
	
	@Override
	public boolean validateToken(String token) {
	
		EntityManager em = factory.createEntityManager();
		String gettoken = "";
		
		try {
            em.getTransaction().begin();
            
            Query q = em.createQuery("SELECT token FROM Userlist u WHERE u.token = '" + token + "'", String.class);
            
//            Query q = em.createQuery("SELECT * FROM userlist u WHERE u.userid = :userid AND u.password = :password", Users.class)
//            		.setParameter("userid", id)
//            		.setParameter("password", pw); 
            
         
            gettoken =  (String) q.getSingleResult();
            
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
		
		if(token.equals(gettoken)) return true;
		else return false;

	}
	
	@Override
	public boolean validateUser(String id, String pw) {
		
		EntityManager em = factory.createEntityManager();
		Userlist user = null;
		Object result = null;
		List<Userlist> userlist = null;
		
		StringBuilder reverse = new StringBuilder();
		reverse.append(pw);
		reverse.reverse();
		pw = reverse.toString();
		
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
		
		//System.out.println("###################" + user.getPassword() + "#############");
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


	
	@Override
	public void storeToken(String userid, String token) {
		
		EntityManager em = factory.createEntityManager();		

		try {
            //em.getTransaction().begin();
            //UPDATE films SET kind = 'Dramatic' WHERE kind = 'Drama';
            //em.createQuery("UPDATE Userlist SET token = '" + token + "' WHERE userid = '" + userid + "'");
            
            Userlist user;
            
			if ((user = em.find(Userlist.class, userid)) != null) {
	            em.getTransaction().begin();
	            user.setToken(token);                              
	            em.getTransaction().commit();
			} else System.out.println("User wasnt found.");
          
            //em.getTransaction().commit();
            System.out.println("Token:" + token + " written for User: " + userid);

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
		
	}



}
