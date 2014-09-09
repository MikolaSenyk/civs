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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.poltava.senyk.civs.model.UserRole;
import ua.poltava.senyk.civs.model.dto.RecommendedPriceDto;
import ua.poltava.senyk.civs.model.dto.UserDto;
import ua.poltava.senyk.civs.utils.HttpUtils;
import ua.poltava.senyk.civs.utils.JsonUtils;

/**
 * RESTful service for recommended prices
 * @author Msenyk
 */
@Controller
@RequestMapping(value="prices")
public class PriceRESTful {
    
    @Autowired
	private AssistanceService _assistanceService;
	@Autowired
	private AuthRESTful _authRESTful;
	
    private static final Logger logger = Logger.getLogger(PriceRESTful.class);
    
	@RequestMapping(value="status", produces="text/plain; charset=utf-8")
	@ResponseBody
	protected String status(HttpServletRequest req, HttpServletResponse res) throws Exception {
		return "Recommended price service is ready\n";
	}
    
    @RequestMapping(value="list/{groupId}", produces="application/json; charset=utf-8")
	@ResponseBody
	protected String groupsList(HttpServletRequest req, @PathVariable Long groupId) {
		JSONObject json = JsonUtils.buildSuccessMessage();
        try {
            JSONArray arr = new JSONArray();
            for (RecommendedPriceDto price: _assistanceService.findPrices(groupId)) {
                arr.add(price.getJSON());
            }
            json.put("items", arr);
        } catch (Exception e) {
            logger.info(e);
            json = JsonUtils.buildErrorMessage(e.getMessage());
        }
		return json.toString() + "\n";
	}
	
	@RequestMapping(value="create", method = RequestMethod.POST, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String create(HttpServletRequest req, HttpServletResponse res) throws Exception {
		JSONObject json;
		HttpUtils httpUtils = new HttpUtils();
		try {
            // FIXME for admin only
            
			// get parameters from JSON body
			String body = httpUtils.getRequestBodyString(req);
            ObjectHelper helper = new ObjectHelper();
            RecommendedPriceDto price = new RecommendedPriceDto();
            price.setSuccess(true);
            helper.UpdateRecommendedPrice(price, body);
            if ( price.isSuccess() ) {
                // create 
                price = _assistanceService.createPrice(price.getGroup().getId(), price.getName(), price.getMeasure(), price.getGradeOne(), price.getGradeTwo(), price.getOutOfSeason());
                json = price.getJSON();
            } else {
                json = JsonUtils.buildErrorMessage(price.getMessageText());
            }
		} catch(Exception e) {
			json = JsonUtils.buildErrorMessage(e.getMessage());
		}
		return json.toString() + "\n";
	}
    
    @RequestMapping(value="{priceId}/update", method = RequestMethod.PUT, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String update(HttpServletRequest req, @PathVariable Long priceId) throws Exception {
        JSONObject json;
		HttpUtils httpUtils = new HttpUtils();
		try {
            // FIXME for admin only
            
			// get parameters from JSON body
            ObjectHelper helper = new ObjectHelper();
            RecommendedPriceDto price = new RecommendedPriceDto();
            price.setSuccess(true);
            helper.UpdateRecommendedPrice(price, httpUtils.getRequestBodyString(req));
            if ( price.isSuccess() ) {
                // update
                _assistanceService.updatePrice(price);
                json = price.getJSON();
            } else {
                json = JsonUtils.buildErrorMessage(price.getMessageText());
            }
		} catch(Exception e) {
			json = JsonUtils.buildErrorMessage(e.getMessage());
		}
		return json.toString() + "\n";
    }
	
	@RequestMapping(value="{priceId}/remove", method = RequestMethod.DELETE, produces="application/json; charset=utf-8")
	@ResponseBody
	protected String remove(HttpServletRequest req, @PathVariable Long priceId) throws Exception {
		JSONObject json = JsonUtils.buildSuccessMessage();
		UserDto authUser = _authRESTful.getLoggedUser(req.getSession());
		if (authUser.getRole() == UserRole.ADMIN) {
			try {
				_assistanceService.removePrice(priceId);
			} catch(Exception e) {
				json = JsonUtils.buildErrorMessage("Unable remove recommended price: " + e.getMessage());
			}
		} else {
			json = JsonUtils.buildErrorMessage("Auth failed");
		}
		return json.toString() + "\n";
	}
}
