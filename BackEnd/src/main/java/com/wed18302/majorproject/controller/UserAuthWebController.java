package com.wed18302.majorproject.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wed18302.majorproject.Authentication;
import com.wed18302.majorproject.interfaces.GenericWebJsonResponse;
import com.wed18302.majorproject.model.User;
import com.wed18302.majorproject.model.UserRepository;
import com.wed18302.majorproject.model.UserType;
import com.wed18302.majorproject.util.JsonErrorResponse;
import com.wed18302.majorproject.util.WebResponseUtil;  

@RestController  
public class UserAuthWebController {  
	
	public final String REGISTRATION_FAILURE = "Registration failure. Account already exists in database.";
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
    @Autowired
    private UserRepository userRepository;
    
    @RequestMapping("/auth/login")  
    @ResponseBody  
    public String loginPassword(HttpServletRequest request) {  

    	String email = request.getParameter("email");
    	String password = request.getParameter("password");

    	// TODO: Improve validation for parameters
    	if (email == null || password == null)
    		return Authentication.generateErrorJson("Some fields were left unfilled or not filled correctly.");
    	    	
    	var user = userRepository.findByEMAIL(email);
    	if (user != null) {
    		if ( passwordEncoder.matches(password, user.getPassword()) ) {
    			return Authentication.generateTokenJson(user);
    		} else {
    			return Authentication.generateErrorJson("Login failure");
    		}
    	}
    	
        return Authentication.generateErrorJson("Account not found.");  
    }
    
    @RequestMapping(value="/auth/getuser", method = RequestMethod.POST)  
    @ResponseBody 
	// /auth/getuser?token={token}
	public ResponseEntity<Object> getUserInfo(@RequestParam("auth-token") String token) throws JsonErrorResponse {
    	return WebResponseUtil.genericWebResponse(null, null, new GenericWebJsonResponse() {

			@Override
			public HashMap<String, Object> getResponse() throws JsonErrorResponse {
				if (token == null || token.length() == 0)
		    		throw new JsonErrorResponse("Invalid token specified.");
				
		    	String decoded = Authentication.decodeToken(token);
		    	if (decoded == null)
		    		throw new JsonErrorResponse("Account not found.");
		    	
		    	User user = userRepository.findByEMAIL(decoded);
		    	if (user == null)
		    		throw new JsonErrorResponse("Account no longer exists in database.");
		    	
		        HashMap<String, Object> hmap = new HashMap<String, Object>();
		        hmap.put(Integer.toString(user.getId()), user);
		        return hmap;
			}

			@Override
			public boolean hasPrivilleges(User user) { // changing this will do nothing, auth is null
				return true;
			}
		});
	}
        
    @RequestMapping("/auth/verifytoken")  
    @ResponseBody  
    public String loginPassword(String token) {  
    	String decoded = Authentication.decodeToken(token);
        return decoded != null ? "1" : "0";  
    }


    @RequestMapping("/auth/register")  
    @ResponseBody  
    public String registerAccount(HttpServletRequest request) {  
    	
    	String email = request.getParameter("email");
    	String password = request.getParameter("password");
    	String firstname = request.getParameter("firstname");
    	String lastname = request.getParameter("lastname");
    	    	
    	String token = RegisterAccount(UserType.Customer, email, password, firstname, lastname);
    	if (token != "") {
    		return token;
    	}
    	
    	return Authentication.generateErrorJson(REGISTRATION_FAILURE);
    }

    @RequestMapping("/auth/registeradmin")  
    @ResponseBody  
    public String registerAdminAccount(HttpServletRequest request) {  
    	
    	String email = request.getParameter("email");
    	String password = request.getParameter("password");
    	String firstname = request.getParameter("firstname");
    	String lastname = request.getParameter("lastname");
    	    	    	
    	String token = RegisterAccount(UserType.Administrator, email, password, firstname, lastname);
    	if (token != "") {
    		return token;
    	}

    	return Authentication.generateErrorJson(REGISTRATION_FAILURE);
    }
    
    public String RegisterAccount(UserType userType, String email, String rawPassword, String firstname, String lastname) {
    	// TODO: Improve validation for parameters
    	if (email == null || rawPassword == null || firstname == null || lastname == null)
    		return "";
    	
    	var user = userRepository.findByEMAIL(email);
    	if (user == null) {
	    	var hash = passwordEncoder.encode(rawPassword);
	        var newUser = new User(email, hash, userType.getValue(), firstname, lastname);
	    	userRepository.save(newUser);
	    	return Authentication.generateTokenJson(newUser);
    	}
    	
    	return ""; // account already exists
    	
    }
    
    
}