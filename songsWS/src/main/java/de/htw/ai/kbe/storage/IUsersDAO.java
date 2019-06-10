package de.htw.ai.kbe.storage;


import java.util.List;
import de.htw.ai.kbe.data.Userlist;

public interface IUsersDAO {
	
	public Userlist getUserByID(String id);
	public List<Userlist> getAllUsers();
	public void saveUser();
	public void deleteCUser();
	public boolean validateUser(String id, String pw);
	public boolean validateToken(String token);

}
