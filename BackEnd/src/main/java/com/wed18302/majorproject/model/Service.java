package com.wed18302.majorproject.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;

    @NotNull(message = "Date of when the booking was created")
    @JsonProperty("createdTimestamp")
    private String CREATEDTIMESTAMP; // when the booking was created
    
    @NotNull(message = "Required")
    private String NAME;

    @NotNull
    @ManyToOne()
    @JoinColumn(name="admin_id")    
    private User ADMIN;

    @ManyToMany
    @JoinTable(name = "worker_services", 
	  joinColumns = @JoinColumn(name = "service_id"), 
	  inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> WORKERS;

    @NotNull
    private String TYPE;

    @NotNull
    private String DESCRIPTION;

    public Service() {
    }

    public Service(String date, User admin, String type, String name, String description) {
    	CREATEDTIMESTAMP = date;
        NAME = name;
        ADMIN = admin;
        TYPE = type;
        DESCRIPTION = description;
        WORKERS = new ArrayList<User>();
    }

    public int getId() {
    	return ID;
    }
    
    public String getName() {
        return NAME;
    }

    public boolean hasAccessLevel(User user, UserType type) {
    	return getAccessLevel(user).getValue() >= type.getValue(); 
    }
    
    public UserType getAccessLevel(User user) {
    	if (getAdmin() == user)
    		return UserType.Administrator;
    	
    	if (getWorkers().contains(user))
    		return UserType.Worker;
    	
    	return UserType.Customer;
    }
    
    @JsonIgnoreProperties({"workerServices", "adminServices"})
    public User getAdmin() {
        return ADMIN;
    }

    public String getType() {
        return TYPE;
    }

    @JsonIgnoreProperties({"workerServices", "adminServices"})
    public List<User> getWorkers() {
    	return this.WORKERS;
    }

    public String getDescription() {
        return DESCRIPTION;
    }
    
}
