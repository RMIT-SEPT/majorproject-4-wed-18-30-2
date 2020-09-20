package com.wed18302.majorproject.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wed18302.majorproject.util.JsonErrorResponse;
import com.wed18302.majorproject.Authentication;
import com.wed18302.majorproject.BookingManager;
import com.wed18302.majorproject.interfaces.GenericWebJsonResponse;
import com.wed18302.majorproject.model.Booking;

@RestController
public class BookingController {

	@Autowired
	Authentication auth;
	
	@Autowired
	BookingManager bookingManager;
	
    @RequestMapping(value="/booking/create", method = RequestMethod.POST)  
    @ResponseBody  
	// Booking-Date-Format: 2020-09-19T09:41:39.808756400Z[UTC]
    public ResponseEntity<Object> booking_Create(@RequestParam("booking-date") String bookingDate, 
    		@RequestParam("customer-email") String customer, 
    		@RequestParam("worker-email") String worker, 
    		@RequestParam("admin-email") String admin ) {

    	return genericWebResponse(new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return serializeToJson(bookingManager.makeBooking(bookingDate, customer, worker, admin));
			}
		});
    }
    
    @RequestMapping(value="/booking/findByCustomer", method = RequestMethod.POST)  
    @ResponseBody  
    public ResponseEntity<Object> booking_FindByCustomer(@RequestParam("customer-email") String customer ) {
    	return genericWebResponse(new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return serializeToJson(bookingManager.findAllForCustomer(customer));
			}
		});
    }
    
    @RequestMapping(value="/booking/findById", method = RequestMethod.POST)  
    @ResponseBody  
    public ResponseEntity<Object> booking_FindById(@RequestParam("booking-id") int bookingId ) {
    	return genericWebResponse(new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return serializeToJson(bookingManager.find(bookingId));
			}
		});
    }
    
    @RequestMapping(value="/booking/delete", method = RequestMethod.POST)  
    @ResponseBody  
    public ResponseEntity<Object> booking_DeleteById(@RequestParam("booking-id") int bookingId ) {
    	return genericWebResponse(new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return serializeToJson(bookingManager.delete(bookingId));
			}
		});
    }

	public static HashMap<String, Object> serializeToJson(List<Booking> bookings) {
        HashMap<String, Object> hmap = new HashMap<String, Object>();
        for (Booking b : bookings)
        	hmap.put(Integer.toString(b.getId()), b);
        return hmap;
	}
    
    public ResponseEntity<Object> genericWebResponse(GenericWebJsonResponse response) {
    	try {
        	/*User user = auth.authenticate(request, UserType.Worker);
        	if (user != null) {
        		
        		//return "Permissions are valid.";
        	}
        	throw new JsonErrorResponse(Authentication.INSUFFICIENT_PERMISSIONS);*/
			return ResponseEntity.ok(response.getResponse());
    	} catch (JsonErrorResponse resp) {
    		return new ResponseEntity<Object>(resp.getMessage(), HttpStatus.OK);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return ResponseEntity.notFound().build();
    }
    

}
