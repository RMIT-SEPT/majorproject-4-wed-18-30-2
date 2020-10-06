package com.wed18302.majorproject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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

public class ServiceManager {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	BookingRepository bookingRepo;
	
	@Autowired
	ServiceRepository serviceRepo;
	
	public List<Service> makeService(int adminId, String type, String name, String description) throws JsonErrorResponse {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));

    	User adminUser = userRepo.findByID(adminId);
    	
    	if (adminUser == null)
    		throw new JsonErrorResponse("Invalid admin id was specified.");

    	Service service = new Service(now.toString(), adminUser, type, name, description);
    	serviceRepo.save(service);

        List<Service> list = new ArrayList<Service>();
        list.add(service);
        return list;
	}
	
	public HashMap<String, Object> assignWorker(int serviceId, int userId)  throws JsonErrorResponse {
		
		Service service = serviceRepo.findByID(serviceId);
    	User workerUser = userRepo.findByID(userId);

    	if (service == null)
    		throw new JsonErrorResponse("Invalid service was specified.");
    	if (workerUser == null)
    		throw new JsonErrorResponse("Invalid user was specified.");
		
    	var workers = service.getWorkers();
    	if (workers.contains(workerUser))
    		throw new JsonErrorResponse("Service already contains worker.");
    	
    	workers.add(workerUser);
    	serviceRepo.save(service);
    	
    	return getServiceMap(service);
	}
	
	public HashMap<String, Object> removeWorker(int serviceId, int userId)  throws JsonErrorResponse {
		
		Service service = serviceRepo.findByID(serviceId);
    	User workerUser = userRepo.findByID(userId);

    	if (service == null)
    		throw new JsonErrorResponse("Invalid service was specified.");
    	if (workerUser == null)
    		throw new JsonErrorResponse("Invalid user was specified.");
		
    	var workers = service.getWorkers();
    	if (!workers.contains(workerUser))
    		throw new JsonErrorResponse("Service doesn't contain worker.");
    	
    	workers.remove(workerUser);
    	serviceRepo.save(service);
    	
    	return getServiceMap(service);
	}
	
	public boolean hasPermission(int serviceId, User user, UserType type) {
		Service service = serviceRepo.findByID(serviceId);

    	if (service != null && user != null)
    	{
    		return service.hasAccessLevel(user, type);
    	}
    		
		return false;
	}
	
	public HashMap<String, Object> getService(int serviceId)  throws JsonErrorResponse {
		
		Service service = serviceRepo.findByID(serviceId);

    	if (service == null)
    		throw new JsonErrorResponse("Invalid service was specified.");
		    	    	
    	return getServiceMap(service);
	}
	
	public HashMap<String, Object> getServices() {
        HashMap<String, Object> hmap = new HashMap<String, Object>();
        for (Service service : serviceRepo.findAll())
        	hmap.put(Integer.toString(service.getId()), service);
		return hmap;
	}
	
	private HashMap<String, Object> getServiceMap(Service service) {
		HashMap<String, Object> hmap = new HashMap<String, Object>();
        hmap.put(Integer.toString(service.getId()), service);
        return hmap;
	}

	public HashMap<String, Object> deleteService(int serviceId) throws JsonErrorResponse {
		
		Service service = serviceRepo.findByID(serviceId);

    	if (service == null)
    		throw new JsonErrorResponse("Invalid service was specified.");
    	
    	List<Booking> bookings = bookingRepo.findbySERVICE(service);
    	for (Booking booking : bookings) {
    		bookingRepo.delete(booking);
    	}
    	
    	serviceRepo.deleteByServiceId(service.getId());
    	
		return new HashMap<String, Object>();
	}
	
}
