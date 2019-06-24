package de.htw.ai.kbe.data;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.htw.ai.kbe.data.Songs.Builder;

@Entity
@Table(name="userlist") 
public class Userlist {


	@Column(name="userid")
	@Id
    private String userid;
    private String firstName;
    private String lastName;    
    private String password;    
    private String token;
    
    @OneToMany(mappedBy="owner", cascade =CascadeType.ALL, orphanRemoval=true)
    private List<SongList> songlists;

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

    public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPassword(String password) {
		this.password = password;
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