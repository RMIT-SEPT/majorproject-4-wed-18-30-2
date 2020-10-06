package com.wed18302.majorproject.tests;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.wed18302.majorproject.BookingManager;
import com.wed18302.majorproject.model.Booking;
import com.wed18302.majorproject.model.BookingRepository;
import com.wed18302.majorproject.model.Service;
import com.wed18302.majorproject.model.ServiceRepository;
import com.wed18302.majorproject.model.User;
import com.wed18302.majorproject.model.UserRepository;
import com.wed18302.majorproject.util.JsonErrorResponse;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BookingTest {
    
    @Autowired
    private BookingManager bookingManager;

	@Autowired
	BookingRepository bookingRepo;
	
	@Autowired
	ServiceRepository serviceRepo;
	
    @Autowired
    private UserRepository userRepository;
   
    @Test
    public void booking_testNewBooking() {

    	// save new test account to the database
    	var customer = new User("test.customer@test.com", "password", 0, "Paul", "Smith");
    	var worker = new User("test.worker@test.com", "password", 0, "Paul", "Smith");
    	var admin = new User("test.admin@test.com", "password", 0, "Paul", "Smith");
    	userRepository.save(customer);
    	userRepository.save(worker);
    	userRepository.save(admin);

		String now = ZonedDateTime.now(ZoneId.of("UTC")).toString();
    	var service = new Service(now.toString(), admin, "Building", "Test Business", "A test description.");
    	service.getWorkers().add(worker);
    	serviceRepo.save(service);
    	String future = ZonedDateTime.now(ZoneId.of("UTC")).plus(10,ChronoUnit.DAYS).toString();
    	try {        	
			List<Booking> bookings = bookingManager.makeBooking(service.getId(), future,
					customer.getId(), worker.getId());
			Assert.assertTrue(bookings.size() == 1);
			
			Booking booking = bookings.get(0);
			Assert.assertTrue(bookingRepo.findByID(booking.getId()) != null);
			bookingManager.delete(booking.getId());
			Assert.assertTrue(bookingRepo.findByID(booking.getId()) == null);
    	} catch (JsonErrorResponse e) {
			e.printStackTrace();
		} finally {
			userRepository.delete(customer);
			userRepository.delete(worker);
			userRepository.delete(admin);
			serviceRepo.delete(service);
		}
    	
    	
    }
    @Test//Will fail
	public void booking_illegal_bookingTime(){
		var customer = new User("test.customer@test.com", "password", 0, "Paul", "Smith");
		var worker = new User("test.worker@test.com", "password", 0, "Paul", "Smith");
		var admin = new User("test.admin@test.com", "password", 0, "Paul", "Smith");
		userRepository.save(customer);
		userRepository.save(worker);
		userRepository.save(admin);
		var service = new Service(ZonedDateTime.now(ZoneId.of("UTC")).toString(), admin, "Building", "Test Business", "A test description.");
		service.getWorkers().add(worker);
		serviceRepo.save(service);
		String past = ZonedDateTime.now(ZoneId.of("UTC")).minus(10, ChronoUnit.DAYS).toString();
		String stats = null;
		try{
			bookingManager.makeBooking(service.getId(), past,customer.getId(), worker.getId());
		}catch (JsonErrorResponse e){
			stats = e.toString();
			e.printStackTrace();
		}
		Assert.assertNotNull(stats);
	}
    
}
