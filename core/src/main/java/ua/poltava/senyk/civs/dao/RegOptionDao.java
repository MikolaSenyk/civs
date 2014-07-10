/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.dao;

import org.springframework.stereotype.Repository;
import ua.poltava.senyk.civs.model.RegOption;

/**
 * DAO for RegOption
 * @author mikola
 */
@Repository
public class RegOptionDao extends Dao<RegOption> {

	public RegOptionDao() {
		setEntityClass(RegOption.class);
	}
	
	public String getPrimaryRegCode() throws Exception {
		RegOption regOption = getEntityManager().find(RegOption.class, RegOption.PRIMARY_REG_OPTION_ID);
		return regOption.getCode();
	}
    
    public String changePrimaryRegCode(String code) throws Exception {
		RegOption regOption = getEntityManager().find(RegOption.class, RegOption.PRIMARY_REG_OPTION_ID);
        regOption.setCode(code);
        getEntityManager().merge(regOption);
		return regOption.getCode();
	}
	
}
