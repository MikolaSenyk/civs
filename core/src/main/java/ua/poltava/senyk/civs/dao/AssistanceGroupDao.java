/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.dao;

import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import ua.poltava.senyk.civs.model.AssistanceGroup;

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
	
	public AssistanceGroup addGroup(String name) throws Exception {
		AssistanceGroup group = new AssistanceGroup(name);
		getEntityManager().persist(group);
		return group;
	}
	
	public AssistanceGroup updateGroup(long id, String name) throws Exception {
		AssistanceGroup group = getById(id);
		if ( group != null ) {
			group.setName(name);
			getEntityManager().merge(group);
		}
		return group;
	}
	
	public void removeGroup(long id) throws Exception {
		AssistanceGroup group = getById(id);
		if ( group != null ) getEntityManager().remove(group);
	}
	
	@SuppressWarnings("unchecked")
	public List<AssistanceGroup> findGroups() throws Exception {
		Query query = getEntityManager().createNamedQuery("AssistanceGroups.findAll");
		return query.getResultList();
	}
    
    @SuppressWarnings("unchecked")
	public List<AssistanceGroup> findGroups(long parentId) throws Exception {
		Query query = getEntityManager().createNamedQuery("AssistanceGroups.findAllParent");
        query.setParameter("parentId", parentId);
		return query.getResultList();
	}
	
}
