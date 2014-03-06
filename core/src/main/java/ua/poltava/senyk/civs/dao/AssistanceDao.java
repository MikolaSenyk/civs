/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.dao;

import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import ua.poltava.senyk.civs.model.Assistance;

/**
 * Assistance DAO implementation
 * @author mikola
 */
@Repository
public class AssistanceDao extends Dao<Assistance> {

	public AssistanceDao() {
		setEntityClass(Assistance.class);
	}
	
	public Assistance getAssistanceById(long id) throws Exception {
		return getEntityManager().find(Assistance.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public boolean isAnyAssistanceByGroupId(long groupId) throws Exception {
		Query query = getEntityManager().createNamedQuery("Assistances.findByGroupId");
		query.setParameter("groupId", groupId);
		query.setMaxResults(1);
		return ! query.getResultList().isEmpty();
	}
	
	@SuppressWarnings("unchecked")
	public List<Assistance> findUserAssistances(long userId) throws Exception {
		Query query = getEntityManager().createNamedQuery("Assistances.findByUserId");
		query.setParameter("userId", userId);
		return query.getResultList();
	}
	
}
