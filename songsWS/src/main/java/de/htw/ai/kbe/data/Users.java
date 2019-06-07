package de.htw.ai.kbe.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

// Klasse heisst wie Tabelle, sonst @Table(name="user") falls user kleingeschrieben
@Entity
@Table(name="usertable") //postgre does not allow "user"
public class Users {

    // kennzeichnet das Identitätsattribut entspricht dem PK (primary key)
    // bedeutet, dass der PK automatisch durch die DB vergeben wird
    // DB hat selbstinkrementelle 
    @Column(name="userid") 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
