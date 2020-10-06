package com.wed18302.majorproject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.wed18302.majorproject.model.Booking;
import com.wed18302.majorproject.model.BookingRepository;
import com.wed18302.majorproject.model.Service;
import com.wed18302.majorproject.model.ServiceRepository;
import com.wed18302.majorproject.model.User;
import com.wed18302.majorproject.model.UserRepository;
import com.wed18302.majorproject.model.UserType;
import com.wed18302.majorproject.util.JsonErrorResponse;

public class BookingManager {

	@Autowired
	ServiceRepository serviceRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	BookingRepository bookingRepo;
	public List<Booking> makeBooking(int serviceId, String bookingDate, 
			int customerId, int workerId) throws JsonErrorResponse {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
    	ZonedDateTime booked = ZonedDateTime.parse(bookingDate);

    	Service service = serviceRepo.findByID(serviceId);
    	User customerUser = userRepo.findByID(customerId);
    	User workerUser = userRepo.findByID(workerId);

    	if (service == null)
    		throw new JsonErrorResponse("Invalid service id was specified.");
    	if (customerUser == null)
    		throw new JsonErrorResponse("Invalid customer id was specified.");
    	if (workerUser == null)
    		throw new JsonErrorResponse("Invalid worker id was specified.");
    	if (!service.getWorkers().contains(workerUser))
    		throw new JsonErrorResponse("Worker does not work for service.");

    	Booking booking = new Booking(service, now, booked, customerUser, workerUser);
    	bookingRepo.save(booking);

        List<Booking> list = new ArrayList<Booking>();
        list.add(booking);
        return list;
	}
	
	public List<Booking> find(int bookingId) throws JsonErrorResponse { 
    	Booking booking = bookingRepo.findByID(bookingId);
    	
    	if (booking == null)
    		throw new JsonErrorResponse("Booking id could not be found in the database.");
    	
        List<Booking> list = new ArrayList<Booking>();
        list.add(booking);
    	
        return list;
	}
	
	public List<Booking> findForCustomer(int customerId) throws JsonErrorResponse {
    	User customerUser = userRepo.findByID(customerId);
    	
    	if (customerUser == null)
    		throw new JsonErrorResponse("Invalid customer id was specified.");
    	
        return bookingRepo.findByCUSTOMER(customerUser);
	}
	
	public List<Booking> delete(int bookingId) throws JsonErrorResponse {

    	//2020-09-19T09:41:39.808756400Z[UTC]	 
    	Booking booking = bookingRepo.findByID(bookingId);
    	
    	if (booking == null)
    		throw new JsonErrorResponse("Booking id could not be found in the database.");
    	
    	bookingRepo.delete(booking);
    	return new ArrayList<Booking>();
	}
	
	public boolean canModifyBooking(int bookingId, User user) {
		try {
	    	Booking booking = bookingRepo.findByID(bookingId);
	    	
	    	if (booking != null)
	    	{
	    		if (booking.getCustomer().equals(user))
	    			return true;
	    		
	    		return booking.getService().hasAccessLevel(user, UserType.Worker);
	    	}
		} catch (Exception e) {
			
		}
    	
    	return false;
	}
		
}
