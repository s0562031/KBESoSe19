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
public class Users {

    // kennzeichnet das Identitätsattribut entspricht dem PK (primary key)
    // bedeutet, dass der PK automatisch durch die DB vergeben wird
    // DB hat selbstinkrementelle 
//    @Column(name="userid") 
//    @Id 
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="userid")
	@Id
    private String userId;

    private String firstName;

    private String lastName;
    
    private String password;

    // noetig
    public Users() {
    }

    public Users(String userId, String firstName, String lastName, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
    
	public Users(Builder build) {
		this.userId = build.userId;		
		this.password = build.password;
		this.firstName = build.firstName;
		this.lastName = build.lastName;
	}


    public String getUserId() {
        return userId;
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
        return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName
                + ", password=" + password + "]";
    }
    
    public static class Builder {
		// required parameter
		private String userId;
		// optional 
		private String password;
		private String firstName;
		private String lastName;

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
		
		public Users build() {
			return new Users(this);
		}

	}
}
