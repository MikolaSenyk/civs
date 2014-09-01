/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.poltava.senyk.civs.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ua.poltava.senyk.civs.config.Base;
import ua.poltava.senyk.civs.dao.AssistanceDao;
import ua.poltava.senyk.civs.dao.AssistanceGroupDao;
import ua.poltava.senyk.civs.dao.UserDao;
import ua.poltava.senyk.civs.model.Assistance;
import ua.poltava.senyk.civs.model.AssistanceGroup;
import ua.poltava.senyk.civs.model.User;
import ua.poltava.senyk.civs.model.dto.AssistanceDto;
import ua.poltava.senyk.civs.model.dto.AssistanceGroupDto;

/**
 * Service for assistances and groups
 * @author mikola
 */
public class AssistanceService {
	
	private static final Logger logger = Logger.getLogger(AssistanceService.class);
	
	@Autowired
	private AssistanceGroupDao _groupDao;
	@Autowired
	private AssistanceDao _assistanceDao;
    @Autowired
    private UserDao _userDao;
    
    // Constants
    public static final int LAST_ASSISTANCES_COUNT = 10;
	
	// Assistance Groups
	
	@Transactional(rollbackFor = Exception.class)
	public List<AssistanceGroupDto> findGroups() throws Exception {
		List<AssistanceGroup> groups = _groupDao.findGroups();
		ObjectHelper helper = new ObjectHelper();
		List<AssistanceGroupDto> dtoGroups = new ArrayList<AssistanceGroupDto>(groups.size());
		for (AssistanceGroup group : groups) {
			dtoGroups.add(helper.getAssistanceGroup(group));
		}
		return dtoGroups;
	}
    
    @Transactional(rollbackFor = Exception.class)
	public List<AssistanceGroupDto> findGroups(long parentId) throws Exception {
		List<AssistanceGroup> groups = _groupDao.findGroups(parentId);
		ObjectHelper helper = new ObjectHelper();
		List<AssistanceGroupDto> dtoGroups = new ArrayList<AssistanceGroupDto>(groups.size());
		for (AssistanceGroup group : groups) {
			dtoGroups.add(helper.getAssistanceGroup(group));
		}
		return dtoGroups;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public AssistanceGroupDto findGroup(long id) throws Exception {
		ObjectHelper helper = new ObjectHelper();
		return helper.getAssistanceGroup(_groupDao.getById(id));
	}
	
	@Transactional(rollbackFor = Exception.class)
	public AssistanceGroupDto createGroup(String name, long parentId) throws Exception {
		AssistanceGroup group = _groupDao.addGroup(name);
        if ( parentId != Base.NULL_LONG_ID ) {
            AssistanceGroup parentGroup = _groupDao.getById(parentId);
            group.setLevel(parentGroup.getLevel()+1);
            group.setParentGroup(parentGroup);
            _groupDao.updateObject(group);
        }
        ObjectHelper helper = new ObjectHelper();
		return helper.getAssistanceGroup(group);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public AssistanceGroupDto updateGroup(long id, String name) throws Exception {
		ObjectHelper helper = new ObjectHelper();
		return helper.getAssistanceGroup(_groupDao.updateGroup(id, name));
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void removeGroup(long id) throws Exception {
		// check existing assistance
		if ( _assistanceDao.isAnyAssistanceByGroupId(id) )
			throw new Exception("Group isn't empty");
		_groupDao.removeGroup(id);
	}
	
	// Assistances
    
    @Transactional(rollbackFor = Exception.class)
	public List<AssistanceDto> findAllAssistances() throws Exception {
		List<AssistanceDto> assistances = new ArrayList<AssistanceDto>();
		ObjectHelper helper = new ObjectHelper();
		for (Assistance assistance: _assistanceDao.findAllAssistances()) {
			assistances.add(helper.getAssistance(assistance));
		}
		return assistances;
	}
    
    @Transactional(rollbackFor = Exception.class)
	public List<AssistanceDto> findLastAssistances() throws Exception {
		List<AssistanceDto> assistances = new ArrayList<AssistanceDto>();
		ObjectHelper helper = new ObjectHelper();
        // FIXME use constant
		for (Assistance assistance: _assistanceDao.findAllEnabled(0, LAST_ASSISTANCES_COUNT)) {
			assistances.add(helper.getAssistance(assistance));
		}
		return assistances;
	}
    
    @Transactional(rollbackFor = Exception.class)
	public List<AssistanceDto> findNotApprovedAssistances() throws Exception {
		List<AssistanceDto> userAssistances = new ArrayList<AssistanceDto>();
		ObjectHelper helper = new ObjectHelper();
		for (Assistance assistance: _assistanceDao.findNotApprovedAssistances()) {
			userAssistances.add(helper.getAssistance(assistance));
		}
		return userAssistances;
	}
    
    @Transactional(rollbackFor = Exception.class)
	public List<AssistanceDto> findNewAssistancesByGroup(long groupId) throws Exception {
		List<AssistanceDto> userAssistances = new ArrayList<AssistanceDto>();
		ObjectHelper helper = new ObjectHelper();
		for (Assistance assistance: _assistanceDao.findAllGroupAssistances(groupId)) {
			userAssistances.add(helper.getAssistance(assistance));
		}
		return userAssistances;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public List<AssistanceDto> findUserAssistances(long userId) throws Exception {
		List<AssistanceDto> userAssistances = new ArrayList<AssistanceDto>();
		ObjectHelper helper = new ObjectHelper();
		for (Assistance assistance: _assistanceDao.findUserAssistances(userId)) {
			userAssistances.add(helper.getAssistance(assistance));
		}
		return userAssistances;
	}
    
    @Transactional(rollbackFor = Exception.class)
	public AssistanceDto getAssistanceById(long assistanceId) throws Exception {
		Assistance assistance = _assistanceDao.getAssistanceById(assistanceId);        
		ObjectHelper helper = new ObjectHelper();
		return helper.getAssistance(assistance);
	}
    
    @Transactional(rollbackFor = Exception.class)
	public AssistanceDto createAssistance(String text, long groupId, long userId) throws Exception {
        User user = _userDao.getUserById(userId);
        AssistanceGroup group = _groupDao.getById(groupId);
		Assistance assistance = new Assistance(user, text);
        _assistanceDao.addObject(assistance);
        // TODO add all related group from current groupId
        
		ObjectHelper helper = new ObjectHelper();
		return helper.getAssistance(assistance);
	}
    
    @Transactional(rollbackFor = Exception.class)
	public AssistanceDto updateAssistance(long assistanceId, String text, long groupId, Boolean approved) throws Exception {
        AssistanceGroup group = _groupDao.getById(groupId);
		Assistance assistance = _assistanceDao.getAssistanceById(assistanceId);
        assistance.setDescription(text);
        assistance.setApproved(approved);
        _assistanceDao.updateObject(assistance);
        // TODO update groups
        
		ObjectHelper helper = new ObjectHelper();
		return helper.getAssistance(assistance);
	}
    
    @Transactional(rollbackFor = Exception.class)
	public void removeAssistance(long id) throws Exception {
        _assistanceDao.deleteObject(id);
	}
    
    @Transactional(rollbackFor = Exception.class)
	public void approveAssistance(long id) throws Exception {
        Assistance assistance = _assistanceDao.getAssistanceById(id);
        assistance.setApproved(true);
        _assistanceDao.updateObject(assistance);
	}
    
    @Transactional(rollbackFor = Exception.class)
	public void enableUserAssistances(long userId) throws Exception {
        for (Assistance assistance: _assistanceDao.findUserAssistances(userId)) {
			assistance.setEnabled(true);
            _assistanceDao.updateObject(assistance);
		}
	}
    
    @Transactional(rollbackFor = Exception.class)
	public void disableUserAssistances(long userId) throws Exception {
        for (Assistance assistance: _assistanceDao.findUserAssistances(userId)) {
			assistance.setEnabled(false);
            _assistanceDao.updateObject(assistance);
		}
	}
	
}
