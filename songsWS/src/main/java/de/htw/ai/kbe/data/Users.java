package de.htw.ai.kbe.bean;

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

    private String email;

    // noetig
    public Users() {
    }

    public Users(String userId, String firstName, String lastName, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName
                + ", email=" + email + "]";
    }
}
