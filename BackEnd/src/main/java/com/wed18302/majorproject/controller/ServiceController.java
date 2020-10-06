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

import com.wed18302.majorproject.Authentication;
import com.wed18302.majorproject.ServiceManager;
import com.wed18302.majorproject.interfaces.GenericWebJsonResponse;
import com.wed18302.majorproject.model.Service;
import com.wed18302.majorproject.model.User;
import com.wed18302.majorproject.model.UserType;
import com.wed18302.majorproject.util.JsonErrorResponse;
import com.wed18302.majorproject.util.WebResponseUtil;

@RestController
public class ServiceController {

	@Autowired
	Authentication auth;
	
	@Autowired
	ServiceManager serviceManager;

    @RequestMapping(value="/service/add", method = RequestMethod.POST)  
    @ResponseBody 
	// addService?adminEmail={email}&type=type&name=name&description=description
	public ResponseEntity<Object> addService(HttpServletRequest request, 
			@RequestParam("admin-id") int adminId, 
			@RequestParam("type") String type, 
			@RequestParam("name") String name, 
			@RequestParam("description") String description) throws JsonErrorResponse {
    	    	
    	return WebResponseUtil.genericWebResponse(request, auth, new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return serializeToJson(serviceManager.makeService(adminId, type, name, description));
			}

			@Override
			public boolean hasPrivilleges(User user) {
				return true;
			}
			
		});
    	
	}
    
    @RequestMapping(value="/service/delete", method = RequestMethod.POST)  
    @ResponseBody 
	// addService?adminEmail={email}&type=type&name=name&description=description
	public ResponseEntity<Object> deleteService(HttpServletRequest request, 
			@RequestParam("service-id") int serviceId) throws JsonErrorResponse {
    	return WebResponseUtil.genericWebResponse(request, auth, new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return serviceManager.deleteService(serviceId);
			}

			@Override
			public boolean hasPrivilleges(User user) {
				return serviceManager.hasPermission(serviceId, user, UserType.Administrator);
			}
		});
	}

    @RequestMapping(value="/service/assignworker", method = RequestMethod.POST)  
    @ResponseBody 
    public ResponseEntity<Object> assignWorker(HttpServletRequest request, 
    		@RequestParam("service-id") int serviceId, 
    		@RequestParam("user-id") int userId) throws JsonErrorResponse {
    	return WebResponseUtil.genericWebResponse(request, auth, new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return serviceManager.assignWorker(serviceId, userId);
			}

			@Override
			public boolean hasPrivilleges(User user) {
				return serviceManager.hasPermission(serviceId, user, UserType.Administrator);
			}
		});
	}
    
    @RequestMapping(value="/service/removeworker", method = RequestMethod.POST)  
    @ResponseBody 
    public ResponseEntity<Object> removeWorker(
    		HttpServletRequest request, 
    		@RequestParam("service-id") int serviceId, 
    		@RequestParam("user-id") int userId) throws JsonErrorResponse {
    	return WebResponseUtil.genericWebResponse(request, auth, new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return serviceManager.removeWorker(serviceId, userId);
			}

			@Override
			public boolean hasPrivilleges(User user) {
				return serviceManager.hasPermission(serviceId, user, UserType.Administrator);
			}
		});
	}

    @RequestMapping(value="/service/get", method = RequestMethod.POST)  
    @ResponseBody  
	public ResponseEntity<Object> getService(
			HttpServletRequest request, 
			@RequestParam("service-id") int serviceId) throws JsonErrorResponse {
    	System.out.println("DEBUG");
    	return WebResponseUtil.genericWebResponse(request, auth, new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return serviceManager.getService(serviceId);
			}

			@Override
			public boolean hasPrivilleges(User user) {
				return true;
			}
		});
	}
    
    @RequestMapping(value="/service/getall", method = RequestMethod.POST)  
    @ResponseBody  
	public ResponseEntity<Object> getServices(HttpServletRequest request) throws JsonErrorResponse {
    	return WebResponseUtil.genericWebResponse(request, auth, new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				return serviceManager.getServices();
			}

			@Override
			public boolean hasPrivilleges(User user) {
				return true;
			}
		});
	}

	public static HashMap<String, Object> serializeToJson(List<Service> services) {
        HashMap<String, Object> hmap = new HashMap<String, Object>();
        for (Service s : services)
        	hmap.put(Integer.toString(s.getId()), s);
        return hmap;
	}
}
