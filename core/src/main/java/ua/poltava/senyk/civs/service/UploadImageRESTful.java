/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.poltava.senyk.civs.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.poltava.senyk.civs.model.dto.UploadedImageDto;
import ua.poltava.senyk.civs.model.dto.UserDto;
import ua.poltava.senyk.civs.utils.HttpUtils;
import ua.poltava.senyk.civs.utils.JsonUtils;

/**
 * RESTful API for deal with upload images
 * @author Msenyk
 */
@Controller
@RequestMapping(value="images")
public class UploadImageRESTful {
    
    @Autowired
	private AppConfig _config;
    @Autowired
    private AuthRESTful _authRESTful;
    @Autowired
    private UploadImageService _imageService;
    @Autowired
    private UserService _userService;
    
    @RequestMapping(value="status", produces="text/plain; charset=utf-8")
	@ResponseBody
	protected String status(HttpServletRequest req, HttpServletResponse res) throws Exception {
		return "Upload image service is ready\n" +
				"Upload dir: " + _config.getUploadBaseDir();
	}
    
    @RequestMapping(value="upload", method = RequestMethod.POST, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String upload(HttpServletRequest req) throws Exception {
		JSONObject json;
		HttpUtils httpUtils = new HttpUtils();
        UserDto authUser = _authRESTful.getLoggedUser(req.getSession());
        if ( authUser != null ) {
            try {
                String body = httpUtils.getRequestBodyString(req);
                JSONObject paramsJson = JSONObject.fromObject(body);
                String imgBody = paramsJson.getString("imgBody");
                String imgExt = paramsJson.getString("imgExt");
                UploadedImageDto imgDto = _imageService.createImage(imgBody, authUser.getId(), imgExt);
                if ( imgDto.isSuccess() ) {
                    json = imgDto.getJSON();
                } else {
                    json = JsonUtils.buildErrorMessage("Unable to upload image");
                }
            } catch(Exception e) {
                json = JsonUtils.buildErrorMessage(e.getMessage());
            }
        } else {
            json = JsonUtils.buildErrorMessage("Auth required");
        }
		return json.toString() + "\n";
	}
    
    @RequestMapping(value="uploadAvatar", method = RequestMethod.POST, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String uploadAvatar(HttpServletRequest req) throws Exception {
		JSONObject json;
		HttpUtils httpUtils = new HttpUtils();
        UserDto authUser = _authRESTful.getLoggedUser(req.getSession());
        if ( authUser != null ) {
            try {
                String body = httpUtils.getRequestBodyString(req);
                JSONObject paramsJson = JSONObject.fromObject(body);
                String imgBody = paramsJson.getString("imgBody");
                String imgExt = paramsJson.getString("imgExt");
                UploadedImageDto imgDto = _imageService.createImage(imgBody, authUser.getId(), imgExt);
                if ( imgDto.isSuccess() ) {
                    // update user profile 
                    authUser.setAvatarPath(imgDto.getPath());
                    _userService.updateUserOptions(authUser);
                    json = imgDto.getJSON();
                } else {
                    json = JsonUtils.buildErrorMessage("Unable to upload avatar");
                }
            } catch(Exception e) {
                json = JsonUtils.buildErrorMessage(e.getMessage());
            }
        } else {
            json = JsonUtils.buildErrorMessage("Auth required");
        }
		return json.toString() + "\n";
	}
    
}
