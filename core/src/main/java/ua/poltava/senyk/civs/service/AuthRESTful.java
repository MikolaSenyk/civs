/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.poltava.senyk.civs.dao.RegOptionDao;
import ua.poltava.senyk.civs.model.UserRole;
import ua.poltava.senyk.civs.model.dto.UserDto;
import ua.poltava.senyk.civs.utils.HttpUtils;
import ua.poltava.senyk.civs.utils.JsonUtils;

/**
 * RESTful API for user's service
 * @author mikola
 */
@Controller
@RequestMapping(value="auth")
public class AuthRESTful {
	
	// HTTP session name for holding UserDto object of authenticated user
	private static final String AUTH_USER_SESSION_NAME = "_authUser";
	private static Logger logger = Logger.getLogger(AuthRESTful.class);
	
	@Autowired
	private AuthService _authService;
	@Autowired
	private RegOptionDao _regOptionDao;
	
	/**
	 * Get authenticated UserDto instance from HTTP session
	 * @param session HTTP session
	 * @return  UserDto or null
	 */
	public UserDto getLoggedUser(HttpSession session) {
		Object obj = session.getAttribute(AUTH_USER_SESSION_NAME);
		if ( obj == null ) return null;
		return ( obj instanceof UserDto ) ? (UserDto) obj : null;
	}
	
	@RequestMapping(value="status", produces="text/plain; charset=utf-8")
	@ResponseBody
	protected String status(HttpServletRequest req, HttpServletResponse res) throws Exception {
		return "Auth service is ready\n";
	}
	
	@RequestMapping(value="login", produces="application/json; charset=utf-8")
	@ResponseBody
	protected String logIn(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String login = req.getParameter("login");
		String passwd = req.getParameter("passwd");
		return this.doLogIn(login, passwd, req).toString() + "\n";
	}
	
	@RequestMapping(value="logout", produces="application/json; charset=utf-8")
	@ResponseBody
	protected String logOut(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.getSession().removeAttribute(AUTH_USER_SESSION_NAME);
		return JsonUtils.getSuccessJsonMessage() + "\n";
	}
	
	@RequestMapping(value="islogged", produces="application/json; charset=utf-8")
	@ResponseBody
	protected String isLogged(HttpServletRequest req, HttpServletResponse res) {
		JSONObject json = JsonUtils.buildSuccessMessage();
		UserDto user = getLoggedUser(req.getSession());
		try {
			if ( user != null ) {
				json.put("id", user.getId());
				json.put("login", user.getLogin());
				json.put("role", user.getRole());
			} else {
				json = JsonUtils.buildErrorMessage("You are not logged in");
			}
		} catch(Exception e) {
			json = JsonUtils.buildErrorMessage(e.getMessage());
		}
		return json.toString() + "\n";
	}
	
	@RequestMapping(value="register", method = RequestMethod.PUT, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String register(HttpServletRequest req, HttpServletResponse res) throws Exception {
		JSONObject json;
		HttpUtils httpUtils = new HttpUtils();
		try {
			// get parameters from JSON body
			String body = httpUtils.getRequestBodyString(req);
			JSONObject paramsJson = JSONObject.fromObject(body);
			String login = paramsJson.getString("login");
			String passwd = paramsJson.getString("passwd");
			String passwdCheck = paramsJson.getString("passwdCheck");
			String code = paramsJson.getString("code");
			
			// validate
			logger.info("Passwds: " + passwd + "/" + passwdCheck);
			if ( passwd == null || !passwd.equals(passwdCheck) ) {
				return JsonUtils.getErrorJsonMessage("Passwords mistmatch");
			}
			String validCode = _regOptionDao.getPrimaryRegCode();
			logger.info("Reg Code: " + validCode + ", code: " + code);
			if ( !validCode.equals(code) ) {
				return JsonUtils.getErrorJsonMessage("Registration code is not valid");
			}

			UserDto user = _authService.register(login, passwd, UserRole.USER, true);
			if ( user.isSuccess() ) {
				json = doLogIn(login, passwd, req);
			} else {
				json = JsonUtils.buildErrorMessage("Register was failed");
			}
		} catch(Exception e) {
			json = JsonUtils.buildErrorMessage(e.getMessage());
		}
		return json.toString() + "\n";
	}
	
	@RequestMapping(value="getRegCode", produces="application/json; charset=utf-8")
	@ResponseBody
	protected String getRegCode(HttpServletRequest req, HttpServletResponse res) {
		JSONObject json = JsonUtils.buildSuccessMessage();
		UserDto user = getLoggedUser(req.getSession());
		if ( user != null ) {
			try {
				if ( user.getRole() == UserRole.ADMIN ) {
					json.put("regCode", _regOptionDao.getPrimaryRegCode());
				} else {
					json = JsonUtils.buildErrorMessage("This information is for admin only");
				}
			} catch(Exception e) {
				json = JsonUtils.buildErrorMessage(e.getMessage());
			}
		} else {
			json = JsonUtils.buildErrorMessage("Auth failed");
		}
		return json.toString() + "\n";
	}
	
	/**
	 * Inner login method. Used from logIn & register methods.
	 * @param login
	 * @param passwd
	 * @param req 
	 * @return JSON answer
	 */
	private JSONObject doLogIn(String login, String passwd, HttpServletRequest req) {
		JSONObject json = JsonUtils.buildSuccessMessage();
		try {
			UserDto user = _authService.login(login, passwd);
			if ( user.isSuccess() ) {
				// save auth in HTTP session
				json.put("id", user.getId());
				json.put("login", user.getLogin());
				json.put("role", user.getRole());
				req.getSession().setAttribute(AUTH_USER_SESSION_NAME, user);
			} else {
				json = JsonUtils.buildErrorMessage("Login failed");
			}
		} catch (Exception e) {
			json = JsonUtils.buildErrorMessage(e.getMessage());
		}
		return json;
	}
}
