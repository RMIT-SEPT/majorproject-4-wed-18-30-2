package com.wed18302.majorproject.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.wed18302.majorproject.model.Booking;
import com.wed18302.majorproject.model.User;

@RestController
public class BookingController {

	@Autowired
	Authentication auth;
	
	@Autowired
	BookingManager bookingManager;
	
    @RequestMapping(value="/booking/create", method = RequestMethod.POST)  
    @ResponseBody  
	// Booking-Date-Format: 2020-09-19T09:41:39.808756400Z[UTC]
    public ResponseEntity<Object> booking_Create(HttpServletRequest request, 
    		@RequestParam("service-id") int serviceId, 
    		@RequestParam("booking-date") String bookingDate, 
    		@RequestParam("customer-id") int customer, 
    		@RequestParam("worker-id") int worker ) {    	
    	return WebResponseUtil.genericWebResponse(request, auth, new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return serializeToJson(bookingManager.makeBooking(serviceId, bookingDate, customer, worker));
			}

			@Override
			public boolean hasPrivilleges(User user) {
				return true;
			}
			
		});
    }
        
    @RequestMapping(value="/booking/findByCustomer", method = RequestMethod.POST)  
    @ResponseBody  
    public ResponseEntity<Object> booking_FindByCustomer(HttpServletRequest request, 
    		@RequestParam("customer-id") int customerId ) {
    	return WebResponseUtil.genericWebResponse(request, auth, new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return serializeToJson(bookingManager.findForCustomer(customerId));
			}

			@Override
			public boolean hasPrivilleges(User user) {
				return true;
			}
		});
    }
    
    @RequestMapping(value="/booking/findById", method = RequestMethod.POST)  
    @ResponseBody  
    public ResponseEntity<Object> booking_FindById(HttpServletRequest request, 
    		@RequestParam("booking-id") int bookingId ) {
    	return WebResponseUtil.genericWebResponse(request, auth, new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return serializeToJson(bookingManager.find(bookingId));
			}

			@Override
			public boolean hasPrivilleges(User user) {
				return true;
			}
		});
    }
    
    @RequestMapping(value="/booking/delete", method = RequestMethod.POST)  
    @ResponseBody  
    public ResponseEntity<Object> booking_DeleteById(HttpServletRequest request, 
    		@RequestParam("booking-id") int bookingId ) {
    	return WebResponseUtil.genericWebResponse(request, auth, new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return serializeToJson(bookingManager.delete(bookingId));
			}

			@Override
			public boolean hasPrivilleges(User user) {
				return bookingManager.canModifyBooking(bookingId, user);
			}
		});
    }

	public static HashMap<String, Object> serializeToJson(List<Booking> bookings) {
        HashMap<String, Object> hmap = new HashMap<String, Object>();
        for (Booking b : bookings)
        	hmap.put(Integer.toString(b.getId()), b);
        return hmap;
	}
    
    

}
