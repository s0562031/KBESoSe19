package de.htw.ai.kbe.storage;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.htw.ai.kbe.data.Songs;
import de.htw.ai.kbe.data.Userlist;

public class InMemoryUserlistDB implements IUsersDAO {
	
    private Map<Integer,Userlist> userStorage;
    
    public InMemoryUserlistDB() {
    	userStorage = userStorage = new ConcurrentHashMap<Integer,Userlist>();
    	initSomeUsers();
    }
    
    private void initSomeUsers() {
    	
    	Userlist myfirstuser = new Userlist.Builder("mmuster","321drowssap")
    			.firstname("Maxime")
    			.lastname("Muster")
    			.token("MzIxZHJvd3NzYXA=")
    			.build();
    	Userlist myseconduser = new Userlist.Builder("eschueler","321drowssap")
    			.firstname("Elena")
    			.lastname("Schuler")
    			.token("MzIxZHJvd3NzYXA=").build();
    	
    	userStorage.put(1,myfirstuser);
    	userStorage.put(2,myseconduser);
    }


	@Override
	public Userlist getUserByID(String id) {
		return userStorage.get(id);
	}

	@Override
	public List<Userlist> getAllUsers() {
		return (List<Userlist>) userStorage.values();
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
	public boolean validateUser(String id, String pw) {
		if(userStorage.get(pw).getPassword().equals(pw)) return true;
		else return false;
	}

	@Override
	public void storeToken(String userid, String token) {
		
	}

	@Override
	public boolean validateToken(String authtoken) {
		
		return true;
		
//		 Iterator it = userStorage.entrySet().iterator();
//		    while (it.hasNext()) {
//		        Map.Entry pair = (Map.Entry)it.next();
//		        System.out.println(pair.getKey() + " = " + pair.getValue());
//		        Userlist curruser = (Userlist) pair.getValue();
//		        if(curruser.getToken().equals(authtoken)) return true;
//		        it.remove(); // avoids a ConcurrentModificationException
//		    }
//		    
//		    return false;
		
	}

	@Override
	public Userlist getUserByToken(String token) {
		return userStorage.get(token);
	}

}
