package com.wed18302.majorproject.tests;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.wed18302.majorproject.ServiceManager;
import com.wed18302.majorproject.model.Service;
import com.wed18302.majorproject.model.ServiceRepository;
import com.wed18302.majorproject.model.User;
import com.wed18302.majorproject.model.UserRepository;
import com.wed18302.majorproject.util.JsonErrorResponse;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ServiceTest {
    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    private ServiceManager serviceManager;

    @Autowired
    private UserRepository userRepository;

    String time_str =ZonedDateTime.now(ZoneId.of("UTC")).toString();
    private void userInitialize (){
        userRepository.save(new User("customer.1@test.com", "password", 0, "customer", "a"));
        userRepository.save(new User("worker.1@test.com", "password", 1, "worker", "a"));
        userRepository.save(new User("worker.2@test.com", "password", 1, "worker", "b"));
        userRepository.save(new User("admin.1@test.com", "password", 2, "admin", "a"));
        userRepository.save(new User("admin.2@test.com", "password", 2, "admin", "b"));
    }

    @Test
    public void services_NewService(){
        userInitialize();
        try{
            List<Service> services = serviceManager.makeService(userRepository.findByEMAIL("admin.1@test.com").getId(),"type1","service1","stuff");
            Assert.assertTrue(services!=null);
            Assert.assertNotNull(serviceRepository.findByID(services.get(0).getId()));


        }catch (JsonErrorResponse e){
            e.printStackTrace();
        }
    }
    @Test
    public void services_assignWorker(){

        userInitialize();
        serviceRepository.save(new Service(time_str,userRepository.findByEMAIL("admin.1@test.com"),"type1","foo","bar"));

        try{
            serviceManager.assignWorker(serviceRepository.findByName("foo").getId(),userRepository.findByEMAIL("worker.1@test.com").getId());
            serviceManager.assignWorker(serviceRepository.findByName("foo").getId(),userRepository.findByEMAIL("worker.2@test.com").getId());
        }catch (JsonErrorResponse e){

            e.printStackTrace();
        }

        Assert.assertTrue(serviceRepository.findByName("foo").getWorkers().get(0).equals(userRepository.findByEMAIL("worker.1@test.com")));
        Assert.assertTrue(serviceRepository.findByName("foo").getWorkers().get(1).equals(userRepository.findByEMAIL("worker.2@test.com")));
    }
    @Test
    public void services_removeWorker(){
        userInitialize();
        serviceRepository.save(new Service(time_str,userRepository.findByEMAIL("admin.1@test.com"),"type1","foo","bar"));

        try{
        	Service service = serviceRepository.findByName("foo");
        	Assert.assertTrue(service != null);
            serviceManager.assignWorker(service.getId(),userRepository.findByEMAIL("worker.1@test.com").getId());
            serviceManager.assignWorker(service.getId(),userRepository.findByEMAIL("worker.2@test.com").getId());
            serviceManager.removeWorker(service.getId(),userRepository.findByEMAIL("worker.1@test.com").getId());
        }catch (JsonErrorResponse e){

            e.printStackTrace();
        }
        boolean b=false;
        serviceRepository.findByName("foo").getWorkers();
        if(serviceRepository.findByName("foo").getWorkers()!=null)
        for(User worker : serviceRepository.findByName("foo").getWorkers()){
            if(worker.getEmail().equals("worker.1@test.com"))
                b=true;
        }
        Assert.assertFalse(b);
    }


}
