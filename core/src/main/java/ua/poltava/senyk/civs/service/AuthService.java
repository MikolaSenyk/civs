/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ua.poltava.senyk.civs.dao.UserDao;
import ua.poltava.senyk.civs.model.User;
import ua.poltava.senyk.civs.model.UserRole;
import ua.poltava.senyk.civs.model.dto.UserDto;

/**
 * Service for work with users authentication
 * @author mikola
 */
public class AuthService {
	
	static private Logger logger = Logger.getLogger(AuthService.class);
	
	@Autowired
	private UserDao _userDao;
	
	@Transactional(rollbackFor = Exception.class)
	public UserDto login(String login, String passwd) throws Exception {
		User user = _userDao.doLogin(login, passwd);
		ObjectHelper helper = new ObjectHelper();
		return helper.getUser(user);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public UserDto register(String login, String passwd, UserRole role, boolean isEnabled) throws Exception {
		User user = _userDao.addUser(login, passwd, role, isEnabled);
		ObjectHelper helper = new ObjectHelper();
		return helper.getUser(user);
	}
        
    @Transactional(rollbackFor = Exception.class)
	public void changePassword(long userId, String pass) throws Exception {
		User user = _userDao.getUserById(userId);
        user.setPasswd(pass);
        _userDao.updateObject(user);
	}
}
