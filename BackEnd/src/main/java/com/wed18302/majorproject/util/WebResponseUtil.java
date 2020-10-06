package com.wed18302.majorproject.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wed18302.majorproject.Authentication;
import com.wed18302.majorproject.interfaces.GenericWebJsonResponse;
import com.wed18302.majorproject.model.User;

public class WebResponseUtil {

    public static ResponseEntity<Object> genericWebResponse(HttpServletRequest request, Authentication auth, GenericWebJsonResponse response) {
    	try {
    		
    		// if null, authentication is not required
    		if (auth != null && !auth.isMasterKey(request)) {
	    		User user = auth.authenticate(request);
	    		if (user == null || !response.hasPrivilleges(user))
	    			throw new JsonErrorResponse(Authentication.INSUFFICIENT_PERMISSIONS);
    		}
    		
			return ResponseEntity.ok(response.getResponse());
    	} catch (JsonErrorResponse resp) {
    		return new ResponseEntity<Object>(resp.getMessage(), HttpStatus.OK);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return ResponseEntity.notFound().build();
    }
    
}
