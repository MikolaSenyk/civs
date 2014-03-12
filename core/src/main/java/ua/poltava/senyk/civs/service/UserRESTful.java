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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.poltava.senyk.civs.model.UserRole;
import ua.poltava.senyk.civs.model.dto.UserDto;
import ua.poltava.senyk.civs.utils.HttpUtils;
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
	
	@RequestMapping(value="{userId}/block", method = RequestMethod.PUT, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String blockUser(HttpServletRequest req, @PathVariable Long userId) throws Exception {
		JSONObject json = JsonUtils.buildSuccessMessage();
		UserDto authUser = _authRESTful.getLoggedUser(req.getSession());
		if (authUser.getRole() == UserRole.ADMIN) {
			try {
				_userService.blockUser(userId);
			} catch(Exception e) {
				json = JsonUtils.buildErrorMessage("Unable block user: " + e.getMessage());
			}
		} else {
			json = JsonUtils.buildErrorMessage("Auth failed");
		}
		return json.toString() + "\n";
	}
	
	@RequestMapping(value="{userId}/unblock", method = RequestMethod.PUT, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String unblockUser(HttpServletRequest req, @PathVariable Long userId) throws Exception {
		JSONObject json = JsonUtils.buildSuccessMessage();
		UserDto authUser = _authRESTful.getLoggedUser(req.getSession());
		if (authUser.getRole() == UserRole.ADMIN) {
			try {
				_userService.unblockUser(userId);
			} catch(Exception e) {
				json = JsonUtils.buildErrorMessage("Unable unblock user: " + e.getMessage());
			}
		} else {
			json = JsonUtils.buildErrorMessage("Auth failed");
		}
		return json.toString() + "\n";
	}
	
	@RequestMapping(value="{userId}", method = RequestMethod.DELETE, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String removeUser(HttpServletRequest req, @PathVariable Long userId) throws Exception {
		JSONObject json = JsonUtils.buildSuccessMessage();
		UserDto authUser = _authRESTful.getLoggedUser(req.getSession());
		if ( authUser.getRole() == UserRole.ADMIN ) {
			try {
				_userService.removeUser(userId);
			} catch(Exception e) {
				json = JsonUtils.buildErrorMessage("Unable to remove user: " + e.getMessage());
			}
		} else {
			json = JsonUtils.buildErrorMessage("Auth failed");
		}
		return json.toString() + "\n";
	}
	
	@RequestMapping(value="get", produces="application/json; charset=utf-8")
	@ResponseBody
	protected String getUser(HttpServletRequest req) throws Exception {
		JSONObject json = JsonUtils.buildSuccessMessage();
		UserDto authUser = _authRESTful.getLoggedUser(req.getSession());
		if ( authUser.getRole() == UserRole.USER ) {
			try {
				UserDto userDto = _userService.findUser(authUser.getId());
				json.put("info", userDto.getJSON());
			} catch(Exception e) {
				json = JsonUtils.buildErrorMessage("Unable to get user info: " + e.getMessage());
			}
		} else {
			json = JsonUtils.buildErrorMessage("User auth required");
		}
		return json.toString() + "\n";
	}
	
	@RequestMapping(value="update", method = RequestMethod.PUT, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String updateUser(HttpServletRequest req) throws Exception {
		JSONObject json = JsonUtils.buildSuccessMessage();
		UserDto authUser = _authRESTful.getLoggedUser(req.getSession());
		HttpUtils httpUtils = new HttpUtils();
		if ( authUser != null ) {
			try {
				ObjectHelper helper = new ObjectHelper();
				helper.updateUserOptions(authUser, httpUtils.getRequestBodyString(req));
				_userService.updateUserOptions(authUser);
			} catch(Exception e) {
				json = JsonUtils.buildErrorMessage("Unable to update user's options: " + e.getMessage());
			}
		} else {
			json = JsonUtils.buildErrorMessage("User auth required");
		}
		return json.toString() + "\n";
	}
	
}
