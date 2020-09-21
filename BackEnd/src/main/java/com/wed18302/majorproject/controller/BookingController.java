package com.wed18302.majorproject.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wed18302.majorproject.util.JsonErrorResponse;
import com.wed18302.majorproject.util.WebResponseUtil;
import com.wed18302.majorproject.Authentication;
import com.wed18302.majorproject.BookingManager;
import com.wed18302.majorproject.interfaces.GenericWebJsonResponse;

@RestController
public class BookingController {

	@Autowired
	Authentication auth;
	
	@Autowired
	BookingManager bookingManager;
	
    @RequestMapping(value="/booking/create", method = RequestMethod.POST)  
    @ResponseBody  
	// Booking-Date-Format: 2020-09-19T09:41:39.808756400Z[UTC]
    public ResponseEntity<Object> booking_Create(
    		@RequestParam("service-id") int serviceId, 
    		@RequestParam("booking-date") String bookingDate, 
    		@RequestParam("customer-id") int customer, 
    		@RequestParam("worker-id") int worker ) {

    	return WebResponseUtil.genericWebResponse(new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return bookingManager.makeBooking(serviceId, bookingDate, customer, worker);
			}
			
		});
    }
        
    @RequestMapping(value="/booking/findByCustomer", method = RequestMethod.POST)  
    @ResponseBody  
    public ResponseEntity<Object> booking_FindByCustomer(@RequestParam("customer-id") int customerId ) {
    	return WebResponseUtil.genericWebResponse(new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return bookingManager.findForCustomer(customerId);
			}
		});
    }
    
    @RequestMapping(value="/booking/findById", method = RequestMethod.POST)  
    @ResponseBody  
    public ResponseEntity<Object> booking_FindById(@RequestParam("booking-id") int bookingId ) {
    	return WebResponseUtil.genericWebResponse(new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return bookingManager.find(bookingId);
			}
		});
    }
    
    @RequestMapping(value="/booking/delete", method = RequestMethod.POST)  
    @ResponseBody  
    public ResponseEntity<Object> booking_DeleteById(@RequestParam("booking-id") int bookingId ) {
    	return WebResponseUtil.genericWebResponse(new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return bookingManager.delete(bookingId);
			}
		});
    }

    

}
