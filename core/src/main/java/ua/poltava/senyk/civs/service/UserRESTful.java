/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.poltava.senyk.civs.model.UserRole;
import ua.poltava.senyk.civs.model.dto.UserDto;
import ua.poltava.senyk.civs.utils.JsonUtils;

/**
 * RESTful API for Users
 * @author mikola
 */
@Controller
@RequestMapping(value="users")
public class UserRESTful {
	
	@Autowired
	private UserService _userService;
	@Autowired
	private AuthRESTful _authRESTful;
	
	@RequestMapping(value="status", produces="text/plain; charset=utf-8")
	@ResponseBody
	protected String status(HttpServletRequest req, HttpServletResponse res) throws Exception {
		return "Users service is ready\n";
	}

	/**
	 * Get list of registered users
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value="list", produces="application/json; charset=utf-8")
	@ResponseBody
	protected String usersList(HttpServletRequest req, HttpServletResponse res) throws Exception {
		JSONObject json = JsonUtils.buildSuccessMessage();
		UserDto authUser = _authRESTful.getLoggedUser(req.getSession());
		if (authUser.getRole() == UserRole.ADMIN) {
			JSONArray users = new JSONArray();
			for (UserDto user: _userService.findUsers()) {
				users.add(user.getJSON());
			}
			json.put("items", users);
		} else {
			json = JsonUtils.buildErrorMessage("Auth failed");
		}
		return json.toString() + "\n";
	}
	
}
