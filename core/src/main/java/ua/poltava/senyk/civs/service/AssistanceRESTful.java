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
import ua.poltava.senyk.civs.model.UserRole;
import ua.poltava.senyk.civs.model.dto.AssistanceDto;
import ua.poltava.senyk.civs.model.dto.UserDto;
import ua.poltava.senyk.civs.utils.HttpUtils;
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
	
    @RequestMapping(value="listAll", produces="application/json; charset=utf-8")
	@ResponseBody
	protected String listAll() {
		JSONObject json = JsonUtils.buildSuccessMessage();
        try {
            List<AssistanceDto> assistances = _assistanceService.findAllAssistances();
            JSONArray itemArray = new JSONArray();
            for (AssistanceDto assistance: assistances) {
                itemArray.add(assistance.getJSON());
            }
            json.put("items", itemArray);
        } catch (Exception e) {
            json = JsonUtils.buildErrorMessage("Error: " + e.getMessage());
        }
		return json.toString() + "\n";
	}
    
    @RequestMapping(value="listLast", produces="application/json; charset=utf-8")
	@ResponseBody
	protected String listLast() {
		JSONObject json = JsonUtils.buildSuccessMessage();
        try {
            List<AssistanceDto> assistances = _assistanceService.findLastAssistances();
            JSONArray itemArray = new JSONArray();
            for (AssistanceDto assistance: assistances) {
                itemArray.add(assistance.getJSON());
            }
            json.put("items", itemArray);
        } catch (Exception e) {
            json = JsonUtils.buildErrorMessage("Error: " + e.getMessage());
        }
		return json.toString() + "\n";
	}
    
    @RequestMapping(value="listNew", produces="application/json; charset=utf-8")
	@ResponseBody
	protected String listNew() {
		JSONObject json = JsonUtils.buildSuccessMessage();
        try {
            List<AssistanceDto> assistances = _assistanceService.findNotApprovedAssistances();
            JSONArray itemArray = new JSONArray();
            for (AssistanceDto assistance: assistances) {
                itemArray.add(assistance.getJSON());
            }
            json.put("items", itemArray);
        } catch (Exception e) {
            json = JsonUtils.buildErrorMessage("Error: " + e.getMessage());
        }
		return json.toString() + "\n";
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
    
    @RequestMapping(value="create", method = RequestMethod.POST, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String create(HttpServletRequest req) {
		JSONObject json = JsonUtils.buildSuccessMessage();
		UserDto user = _authRESTful.getLoggedUser(req.getSession());
		if ( user.isSuccess() && user.isEnabled() ) {
            HttpUtils httpUtils = new HttpUtils();
			try {
                String body = httpUtils.getRequestBodyString(req);
                JSONObject paramsJson = JSONObject.fromObject(body);
                String description = paramsJson.getString("description");
                long groupId = paramsJson.getLong("groupId");
				AssistanceDto assistance = _assistanceService.createAssistance(description, groupId, user.getId());
				json.put("item", assistance.getJSON());
			} catch (Exception e) {
				json = JsonUtils.buildErrorMessage("Error: " + e.getMessage());
			}
		} else {
			json = JsonUtils.buildErrorMessage("Auth required");
		}
		return json.toString() + "\n";
	}
    
    @RequestMapping(value="{assistanceId}", method = RequestMethod.DELETE, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String remove(HttpServletRequest req, @PathVariable Long assistanceId) throws Exception {
		JSONObject json = JsonUtils.buildSuccessMessage();
		UserDto authUser = _authRESTful.getLoggedUser(req.getSession());
        try {
            AssistanceDto assistance = _assistanceService.getAssistanceById(assistanceId);
            if ( authUser.getRole() == UserRole.ADMIN || assistance.getUser().getId() == authUser.getId() ) {
                _assistanceService.removeAssistance(assistanceId);
            } else {
                json = JsonUtils.buildErrorMessage("Auth failed");
            }
        } catch(Exception e) {
            json = JsonUtils.buildErrorMessage("Unable to remove assistance: " + e.getMessage());
        }
		return json.toString() + "\n";
	}
    
    @RequestMapping(value="{assistanceId}/approve", method = RequestMethod.PUT, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String approve(HttpServletRequest req, @PathVariable Long assistanceId) throws Exception {
		JSONObject json = JsonUtils.buildSuccessMessage();
		UserDto authUser = _authRESTful.getLoggedUser(req.getSession());
        try {
            if ( authUser.getRole() == UserRole.ADMIN ) {
                _assistanceService.approveAssistance(assistanceId);
            } else {
                json = JsonUtils.buildErrorMessage("Auth failed");
            }
        } catch(Exception e) {
            json = JsonUtils.buildErrorMessage("Unable to approve assistance: " + e.getMessage());
        }
		return json.toString() + "\n";
	}
    
    @RequestMapping(value="enable/all", method = RequestMethod.POST, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String enableAll(HttpServletRequest req) {
		JSONObject json = JsonUtils.buildSuccessMessage();
		UserDto authUser = _authRESTful.getLoggedUser(req.getSession());
		if ( authUser.isSuccess() && authUser.isEnabled() && authUser.getRole() == UserRole.USER ) {
			try {
				_assistanceService.enableUserAssistances(authUser.getId());
			} catch (Exception e) {
				json = JsonUtils.buildErrorMessage("Error: " + e.getMessage());
			}
		} else {
			json = JsonUtils.buildErrorMessage("User auth required");
		}
		return json.toString() + "\n";
	}
    
    @RequestMapping(value="disable/all", method = RequestMethod.POST, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String disableAll(HttpServletRequest req) {
		JSONObject json = JsonUtils.buildSuccessMessage();
		UserDto authUser = _authRESTful.getLoggedUser(req.getSession());
		if ( authUser.isSuccess() && authUser.isEnabled() && authUser.getRole() == UserRole.USER ) {
			try {
				_assistanceService.disableUserAssistances(authUser.getId());
			} catch (Exception e) {
				json = JsonUtils.buildErrorMessage("Error: " + e.getMessage());
			}
		} else {
			json = JsonUtils.buildErrorMessage("User auth required");
		}
		return json.toString() + "\n";
	}
    
    
}
