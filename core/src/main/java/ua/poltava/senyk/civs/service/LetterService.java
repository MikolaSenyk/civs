/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.poltava.senyk.civs.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ua.poltava.senyk.civs.dao.LetterDao;
import ua.poltava.senyk.civs.dao.UserDao;
import ua.poltava.senyk.civs.model.Letter;
import ua.poltava.senyk.civs.model.User;
import ua.poltava.senyk.civs.model.dto.LetterDto;

/**
 * Service for Letter
 * @author Msenyk
 */
public class LetterService {
    
    private static final Logger logger = Logger.getLogger(LetterService.class);
    
    @Autowired
    private LetterDao _letterDao;
    @Autowired
    private UserDao _userDao;
    @Autowired
    private AppConfig _config;
        
    @Transactional(rollbackFor = Exception.class)
    public LetterDto sendToAdmin(String msg, long userId) throws Exception {
        User fromUser = _userDao.getUserById(userId);
        User toUser = _userDao.getUserById(_config.getAdminUserId());
        Letter l = new Letter(fromUser, toUser, msg);
        _letterDao.addObject(l);
        ObjectHelper helper = new ObjectHelper();
        return helper.getLetter(l);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public List<LetterDto> findNewLettersToUser(long userId) throws Exception {
        List<LetterDto> letters = new ArrayList<LetterDto>();
        ObjectHelper helper = new ObjectHelper();
        for (Letter l: _letterDao.findNewToUser(userId)) {
            letters.add(helper.getLetter(l));
        }
        return letters;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(long letterId, long userId) throws Exception {
        Letter letter = _letterDao.getLetterById(letterId);
        if ( letter != null && letter.getToUser().getId() == userId ) {
            _letterDao.markAsRead(letterId);
        }
    }
    
}
