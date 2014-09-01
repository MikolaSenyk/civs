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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.poltava.senyk.civs.config.Base;
import ua.poltava.senyk.civs.model.UserRole;
import ua.poltava.senyk.civs.model.dto.AssistanceGroupDto;
import ua.poltava.senyk.civs.model.dto.UserDto;
import ua.poltava.senyk.civs.utils.HttpUtils;
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
	@Autowired
	private AuthRESTful _authRESTful;
	
	@RequestMapping(value="status", produces="text/plain; charset=utf-8")
	@ResponseBody
	protected String status(HttpServletRequest req, HttpServletResponse res) throws Exception {
		return "Assistance group service is ready\n";
	}
	
	@RequestMapping(value="list/{parentId}", produces="application/json; charset=utf-8")
	@ResponseBody
	protected String groupsList(HttpServletRequest req, @PathVariable Long parentId) throws Exception {
		JSONObject json = JsonUtils.buildSuccessMessage();
		JSONArray groups = new JSONArray();
        List<AssistanceGroupDto> groupList;
        if ( parentId == Base.NULL_LONG_ID ) {
            json.put("hasParent", false);
        } else {
            json.put("hasParent", true);
            json.put("parent", _assistanceService.findGroup(parentId).getJSON());
        }
        // add items
        groupList = _assistanceService.findGroups(parentId);
		for (AssistanceGroupDto group: groupList) {
			groups.add(group.getJSON());
		}
		json.put("items", groups);
		return json.toString() + "\n";
	}
	
	@RequestMapping(value="create", method = RequestMethod.PUT, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String create(HttpServletRequest req, HttpServletResponse res) throws Exception {
		JSONObject json;
		HttpUtils httpUtils = new HttpUtils();
		try {
			// get parameters from JSON body
			String body = httpUtils.getRequestBodyString(req);
			JSONObject paramsJson = JSONObject.fromObject(body);
			String name = paramsJson.getString("name");
            Long parentId = paramsJson.getLong("parentId");
			// TODO validate
			
			AssistanceGroupDto group = _assistanceService.createGroup(name, parentId);
			json = group.getJSON();
		} catch(Exception e) {
			json = JsonUtils.buildErrorMessage(e.getMessage());
		}
		return json.toString() + "\n";
	}
	
	@RequestMapping(value="{groupId}/remove", method = RequestMethod.PUT, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String remove(HttpServletRequest req, @PathVariable Long groupId) throws Exception {
		JSONObject json = JsonUtils.buildSuccessMessage();
		UserDto authUser = _authRESTful.getLoggedUser(req.getSession());
		if (authUser.getRole() == UserRole.ADMIN) {
			try {
				_assistanceService.removeGroup(groupId);
			} catch(Exception e) {
				json = JsonUtils.buildErrorMessage("Unable remove assistance group: " + e.getMessage());
			}
		} else {
			json = JsonUtils.buildErrorMessage("Auth failed");
		}
		return json.toString() + "\n";
	}
	
}
