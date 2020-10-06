package com.wed18302.majorproject.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;
    
    @Email(message = "Email is mandatory")
    private String EMAIL;
    @NotNull(message = "Password is mandatory")
    private String PASSWORD;
    @NotNull(message = "First name is mandatory")
    private String FIRSTNAME;
    @NotNull(message = "Last name is mandatory")
    private String LASTNAME;

    @OneToMany(mappedBy="ADMIN",cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Service> ADMINSERVICES;
    
    @ManyToMany(mappedBy = "WORKERS")
    private List<Service> WORKERSERVICES;
    
    public User() {
    }
    
    public User(String email, String password, int type, String firstname, String lastname) {
    	this.EMAIL = email;
    	this.PASSWORD = password;
    	this.FIRSTNAME = firstname;
    	this.LASTNAME = lastname;
    }
    
    public int getId() {
    	return this.ID;
    }
    
    @JsonIgnore
    public String getEmail() {
    	return this.EMAIL;
    }

    @JsonIgnore
    public String getPassword() {
    	return this.PASSWORD;
    }
    
    public String getFirstName() {
    	return this.FIRSTNAME;
    }

    @JsonIgnore
    public String getLastName() {
    	return this.LASTNAME;
    }

    @JsonIgnoreProperties("workers")
    public List<Service> getWorkerServices() {
    	return this.WORKERSERVICES;
    }

    @JsonIgnoreProperties("admin")
    public List<Service> getAdminServices() {
    	return this.ADMINSERVICES;
    }
            
    @Override
    public String toString() {
    	return String.format("ID: %d, Email: %s", this.ID, this.EMAIL);
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (obj.getClass() == User.class)
    	{
    		User u = (User)obj;
    		return u.ID == this.ID && u.EMAIL == u.EMAIL;
    	}
    	return true;
    }
    
}
