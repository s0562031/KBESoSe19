package de.htw.ai.kbe.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import de.htw.ai.kbe.data.Songs.Builder;

// Klasse heisst wie Tabelle, sonst @Table(name="user") falls user kleingeschrieben
@Entity
@Table(name="userlist") //postgre does not allow "user"
public class Userlist {

    // kennzeichnet das Identitätsattribut entspricht dem PK (primary key)
    // bedeutet, dass der PK automatisch durch die DB vergeben wird
    // DB hat selbstinkrementelle 
//    @Column(name="userid") 
//    @Id 
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="userid")
	@Id
    private String userid;

    private String firstName;

    private String lastName;
    
    private String password;
    
    private String token;

    // noetig
    public Userlist() {
    }

    public Userlist(String userId, String firstName, String lastName, String password, String token) {
        this.userid = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.token = token;
    }
    
	public Userlist(Builder build) {
		this.userid = build.userId;		
		this.password = build.password;
		this.firstName = build.firstName;
		this.lastName = build.lastName;
		this.token = build.token;
	}


    public String getUserId() {
        return userid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String setFirstName(String firstName) {
        return this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User [userId=" + userid + ", firstName=" + firstName + ", lastName=" + lastName
                + ", password=" + password + " token= " + token + "]";
    }
    
    public static class Builder {
		// required parameter
		private String userId;
		// optional 
		private String password;
		private String firstName;
		private String lastName;
		private String token;

		public Builder(String userId, String password) {
			this.userId = userId;
			this.password = password;
		}
		
		public Builder password(String val) {
			password=val;
			return this;
		}
		
		public Builder firstname(String val) {
			firstName = val;
			return this;
		}
		
		public Builder lastname(String val) {
			lastName = val;
			return this;
		}
		
		public Builder token(String val) {
			token = val;
			return this;
		}
		
		public Userlist build() {
			return new Userlist(this);
		}
		


	}
}
