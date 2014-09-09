/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.poltava.senyk.civs.dao;

import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import ua.poltava.senyk.civs.model.RecommendedPrice;

/**
 * DAO for recommended prices
 * @author Msenyk
 */
@Repository
public class RecommendedPriceDao extends Dao<RecommendedPrice> {

    public RecommendedPriceDao() {
        setEntityClass(RecommendedPrice.class);
    }
    
    @SuppressWarnings("unchecked")
    public List<RecommendedPrice> findByGroup(long groupId) throws Exception {
        Query query = getEntityManager().createNamedQuery("RecommendedPrices.findByGroup");
        query.setParameter("groupId", groupId);
        return query.getResultList();
    }
    
}
