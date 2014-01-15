/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.dao;

import java.util.List;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ua.poltava.senyk.civs.model.User;
import ua.poltava.senyk.civs.model.UserRole;

/**
 * User DAO implementation
 * @author mikola
 */
@Repository
public class UserDao extends Dao<User> {
	
	private Logger logger = Logger.getLogger(UserDao.class);

	public UserDao() {
		setEntityClass(User.class);
	}
	
	public User getUserById(long id) throws Exception {
		return getEntityManager().find(User.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public User findUserByLogin(String login) throws Exception {
		Query query = getEntityManager().createNamedQuery("Users.findByLogin");
		query.setParameter("login", login);
		query.setMaxResults(1);
		List list = query.getResultList();
		if ( list.isEmpty() ) return null;
		return (User) list.get(0);
	}
	
	public User doLogin(String login, String passwd) throws Exception {
		Query query = getEntityManager().createNamedQuery("Users.doLogin");
		query.setParameter("login", login);
		query.setParameter("passwd", passwd);
		query.setMaxResults(1);
		List list = query.getResultList();
		if ( list.isEmpty() ) return null;
		return (User) list.get(0);
	}
	
	public User addUser(String login, String passwd, UserRole role, boolean isEnabled) throws Exception {
		User user = new User(login, passwd);
		user.setRole(role);
		user.setEnabled(isEnabled);
		validateUser(user);
		getEntityManager().persist(user);
		return user;
	}
	
	private void validateUser(User user) throws Exception {
		if ( user.getLogin() == null ) throw new Exception("Login is empty");
		if ( user.getLogin().length() < 3 )	throw new Exception("User login too short");
		if ( user.getLogin().length() > 32 ) throw new Exception("User login too long (32 symbols MAX)");
		if ( user.getPasswd() == null ) throw new Exception("Password is empty");
		if ( user.getPasswd().length() < 3 ) throw new Exception("Password too short");
		if ( user.getPasswd().length() > 32 ) throw new Exception("Password too long (32 symbols MAX)");
	}
	
}
