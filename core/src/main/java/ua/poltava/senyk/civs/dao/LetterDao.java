/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.poltava.senyk.civs.dao;

import java.util.List;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ua.poltava.senyk.civs.model.Letter;

/**
 * DAO for Letter entity
 * @author Msenyk
 */
@Repository
public class LetterDao extends Dao<Letter> {
	
	private final Logger logger = Logger.getLogger(LetterDao.class);

    public LetterDao() {
        setEntityClass(LetterDao.class);
    }
    
    public Letter getLetterById(long id) throws Exception {
        return getEntityManager().find(Letter.class, id);
    }
    
    public void markAsRead(long letterId) throws Exception {
        Letter letter = getLetterById(letterId);
        if ( letter != null ) {
            letter.setRead(true);
            getEntityManager().merge(letter);
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Letter> findNewToUser(long userId) throws Exception {
        Query query = getEntityManager().createNamedQuery("Letters.findNewToUser");
		query.setParameter("userId", userId);
        return query.getResultList();
    }
    
    
    
    
}
