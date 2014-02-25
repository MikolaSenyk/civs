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
import ua.poltava.senyk.civs.model.dto.AssistanceGroupDto;
import ua.poltava.senyk.civs.model.dto.UserDto;
import ua.poltava.senyk.civs.utils.JsonUtils;

/**
 * RESTful web service for assistance groups
 * @author mikola
 */
@Controller
@RequestMapping(value="ags")
public class AgRESTful {
	
	@Autowired
	private AssistanceService _assistanceService;
	
	@RequestMapping(value="status", produces="text/plain; charset=utf-8")
	@ResponseBody
	protected String status(HttpServletRequest req, HttpServletResponse res) throws Exception {
		return "Assistance group service is ready\n";
	}
	
	@RequestMapping(value="list", produces="application/json; charset=utf-8")
	@ResponseBody
	protected String groupsList(HttpServletRequest req, HttpServletResponse res) throws Exception {
		JSONObject json = JsonUtils.buildSuccessMessage();
		JSONArray groups = new JSONArray();
		for (AssistanceGroupDto group: _assistanceService.findGroups()) {
			groups.add(group.getJSON());
		}
		json.put("items", groups);
		return json.toString() + "\n";
	}
	
}
