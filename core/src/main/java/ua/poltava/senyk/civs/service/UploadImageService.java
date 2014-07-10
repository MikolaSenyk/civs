/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.poltava.senyk.civs.service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ua.poltava.senyk.civs.dao.UploadedImageDao;
import ua.poltava.senyk.civs.dao.UserDao;
import ua.poltava.senyk.civs.model.UploadedImage;
import ua.poltava.senyk.civs.model.User;
import ua.poltava.senyk.civs.model.dto.UploadedImageDto;

/**
 * Service for work with uploaded images
 * @author Msenyk
 */
public class UploadImageService {
    
    private static final Logger logger = Logger.getLogger(UploadImageService.class);
    
    @Autowired
    private UploadedImageDao _imageDao;
    @Autowired
    private UserDao _userDao;
    @Autowired
    private AppConfig _config;
    
    public String getCurrentFolder() {
        // TODO make more complex logic
        return "2014";
    }
    
    @Transactional(rollbackFor = Exception.class)
    public UploadedImageDto createImage(String imgBody, long userId, String ext) throws Exception {
        User user = _userDao.getUserById(userId);
        UploadedImage img = new UploadedImage(user, getCurrentFolder(), ext);
        _imageDao.addObject(img);
        ObjectHelper helper = new ObjectHelper();
        UploadedImageDto imgDto = helper.getUploadedImage(img);
        // save file
        OutputStream stream = new FileOutputStream(_config.getUploadBaseDir() + imgDto.getJSON().getString("path"));
        try {
            byte[] data = Base64.decodeBase64(imgBody);
            stream.write(data);
        } finally {
            stream.close();
        }
        return imgDto;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void removeImage(long imgId) throws Exception {
        UploadedImage img = _imageDao.findImageById(imgId);
        if ( img != null ) {
            _imageDao.deleteObject(img);
        }
    }
}
