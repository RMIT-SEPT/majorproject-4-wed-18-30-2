package com.wed18302.majorproject.interfaces;

import java.util.HashMap;

import com.wed18302.majorproject.model.User;
import com.wed18302.majorproject.util.JsonErrorResponse;

public interface GenericWebJsonResponse {

	//public boolean hasAccessLevel(User user) throws JsonErrorResponse;
	public boolean hasPrivilleges(User user);
	public HashMap<String, Object> getResponse() throws JsonErrorResponse;
	
}
