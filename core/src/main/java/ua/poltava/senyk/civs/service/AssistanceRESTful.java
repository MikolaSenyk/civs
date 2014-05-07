/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.poltava.senyk.civs.model.dto.AssistanceDto;
import ua.poltava.senyk.civs.model.dto.UserDto;
import ua.poltava.senyk.civs.utils.JsonUtils;

/**
 * RESTful web service for Assistances
 * @author mikola
 */
@Controller
@RequestMapping(value="assistances")
public class AssistanceRESTful {
	
	@Autowired
	private AssistanceService _assistanceService;
	@Autowired
	private AuthRESTful _authRESTful;
	
	@RequestMapping(value="status", produces="text/plain; charset=utf-8")
	@ResponseBody
	protected String status(HttpServletRequest req, HttpServletResponse res) throws Exception {
		return "Assistance service is ready\n";
	}
	
	@RequestMapping(value="listByUser", produces="application/json; charset=utf-8")
	@ResponseBody
	protected String listByUser(HttpServletRequest req) {
		JSONObject json = JsonUtils.buildSuccessMessage();
		UserDto user = _authRESTful.getLoggedUser(req.getSession());
		if ( user.isSuccess() && user.isEnabled() ) {
			try {
				// find all assistances for logged user
				List<AssistanceDto> assistances = _assistanceService.findUserAssistances(user.getId());
				JSONArray itemArray = new JSONArray();
				for (AssistanceDto assistance: assistances) {
					itemArray.add(assistance.getJSON());
				}
				json.put("items", itemArray);
			} catch (Exception e) {
				json = JsonUtils.buildErrorMessage("Error: " + e.getMessage());
			}
		} else {
			json = JsonUtils.buildErrorMessage("Auth required");
		}
		return json.toString() + "\n";
	}
	
}
