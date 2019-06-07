package de.htw.ai.kbe.storage;


import java.util.List;
import de.htw.ai.kbe.data.Users;

public interface IUsersDAO {
	
	public Users getUserByID(int id);
	public List<Users> getAllUsers();
	public void saveUser();
	public void deleteCUser();
	public String getToken(String userid, String pw);

}
