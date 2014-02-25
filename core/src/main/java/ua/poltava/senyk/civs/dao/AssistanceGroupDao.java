/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.dao;

import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import ua.poltava.senyk.civs.model.AssistanceGroup;
import ua.poltava.senyk.civs.model.User;

/**
 * DAO for assisance groups
 * @author mikola
 */
@Repository
public class AssistanceGroupDao extends Dao<AssistanceGroup> {

	public AssistanceGroupDao() {
		setEntityClass(AssistanceGroup.class);
	}
	
	public AssistanceGroup getById(long id) throws Exception {
		return this.getEntityManager().find(AssistanceGroup.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<AssistanceGroup> findGroups() throws Exception {
		Query query = getEntityManager().createNamedQuery("AssistanceGroups.findAll");
		return query.getResultList();
	}
	
}
