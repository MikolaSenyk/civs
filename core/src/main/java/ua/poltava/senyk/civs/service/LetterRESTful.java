/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.poltava.senyk.civs.model.UserRole;
import ua.poltava.senyk.civs.model.dto.LetterDto;
import ua.poltava.senyk.civs.model.dto.UserDto;
import ua.poltava.senyk.civs.utils.HttpUtils;
import ua.poltava.senyk.civs.utils.JsonUtils;

/**
 * RESTful API for Letter (messaging system)
 * @author Msenyk
 */
@Controller
@RequestMapping(value="letter")
public class LetterRESTful {
    
    @Autowired
    private LetterService _letterService;
    @Autowired
    private AuthRESTful _authRESTful;
    
    @RequestMapping(value="status", produces="text/plain; charset=utf-8")
	@ResponseBody
	protected String status(HttpServletRequest req, HttpServletResponse res) throws Exception {
		return "Letter service is ready\n";
	}
    
    @RequestMapping(value="sendToAdmin", method = RequestMethod.POST, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String sendToAdmin(HttpServletRequest req) throws Exception {
		JSONObject json = JsonUtils.buildSuccessMessage();
		HttpUtils httpUtils = new HttpUtils();
        UserDto authUser = _authRESTful.getLoggedUser(req.getSession());
        if ( authUser != null ) {
            try {
                String body = httpUtils.getRequestBodyString(req);
                JSONObject paramsJson = JSONObject.fromObject(body);
                String msg = paramsJson.getString("message");
                LetterDto letter = _letterService.sendToAdmin(msg, authUser.getId());
                if ( letter.isSuccess() ) {
                    json.put("letterId", letter.getId());
                } else {
                    json = JsonUtils.buildErrorMessage("Unable to send letter to admin");
                }
            } catch(Exception e) {
                json = JsonUtils.buildErrorMessage(e.getMessage());
            }
        } else {
            json = JsonUtils.buildErrorMessage("Auth required");
        }
		return json.toString() + "\n";
	}
    
    @RequestMapping(value="listNew", method = RequestMethod.POST, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String listNew(HttpServletRequest req) throws Exception {
		JSONObject json = JsonUtils.buildSuccessMessage();
        UserDto authUser = _authRESTful.getLoggedUser(req.getSession());
        if ( authUser != null && authUser.getRole() == UserRole.ADMIN ) {
            try {
                List<LetterDto> letters = _letterService.findNewLettersToUser(authUser.getId());
                JSONArray items = new JSONArray();
                for (LetterDto letter: letters) {
                    items.add(letter.getJSON());
                }
                json.put("items", items);
            } catch(Exception e) {
                json = JsonUtils.buildErrorMessage(e.getMessage());
            }
        } else {
            json = JsonUtils.buildErrorMessage("Admin auth required");
        }
		return json.toString() + "\n";
	}
}
