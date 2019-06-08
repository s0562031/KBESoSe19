package test;

import java.util.LinkedList;
import java.util.List;

import de.htw.ai.kbe.services.TokenHandler;
import de.htw.ai.kbe.storage.IUsersDAO;
import de.htw.ai.kbe.storage.UsersDBDAO;

public class TokenTester {
	
	public static void main(String[] args) {
		
		TokenHandler th = new TokenHandler();
		List<String> tokenlist = new LinkedList<String>();
		
		tokenlist.add(th.generateToken("a", "password123"));
		tokenlist.add(th.generateToken("b", "password123"));
		tokenlist.add(th.generateToken("c", "password123"));
		tokenlist.add(th.generateToken("d", "password123"));
		tokenlist.add(th.generateToken("e", "kaufoiaf"));
		tokenlist.add(th.generateToken("f", "321drowssap"));

		
		for(String elem: tokenlist) {
			System.out.println(elem);
		}
		
		System.out.println("as token: " + th.getTokenByID("a"));
		System.out.println(th.getTokenByID("b"));
			
		
		
	}

}
