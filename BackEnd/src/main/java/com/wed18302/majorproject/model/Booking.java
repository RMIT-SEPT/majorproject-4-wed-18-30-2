package com.wed18302.majorproject.model;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;
    
    @NotNull(message = "Date of when the booking was created")
    @JsonProperty("createdTimestamp")
    private String CREATEDTIMESTAMP; // when the booking was created
    @NotNull(message = "Date of when the booking will be is mandatory")
    @JsonProperty("bookingTimestamp")
    private String BOOKINGTIMESTAMP; // when the booking is set for
    
    @NotNull
    @JsonProperty("serviceId")
    @OneToOne(fetch = FetchType.EAGER)
    private Service SERVICE;
    
    @NotNull
    @JsonProperty("customerId")
    @ManyToOne(fetch = FetchType.EAGER)
    private User CUSTOMER;
    
    @NotNull
    @JsonProperty("workerId")
    @ManyToOne(fetch = FetchType.EAGER)
    private User WORKER;
        
    public int getId() {
    	return this.ID;
    }

    @JsonIgnore
    public User getCustomer() {
    	return CUSTOMER;
    }
    
    @JsonIgnore
    public Service getService() {
    	return SERVICE;
    }
    
    public Booking() {
    }
    
    public Booking(Service service, ZonedDateTime created, ZonedDateTime booking, User customer, User worker) {
    	this.SERVICE = service;
    	this.CREATEDTIMESTAMP = created.toString();
    	this.BOOKINGTIMESTAMP = booking.toString();
    	this.CUSTOMER = customer;
    	this.WORKER = worker;
    }
            
}
