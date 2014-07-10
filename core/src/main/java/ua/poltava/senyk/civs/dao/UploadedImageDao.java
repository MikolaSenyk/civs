/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.poltava.senyk.civs.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ua.poltava.senyk.civs.model.UploadedImage;

/**
 * DAO for UploadedImage entity
 * @author Msenyk
 */
@Repository
public class UploadedImageDao extends Dao<UploadedImage>{
    
    private final Logger logger = Logger.getLogger(UploadedImageDao.class);

    public UploadedImageDao() {
        setEntityClass(UploadedImage.class);
    }
    
    public UploadedImage findImageById(long id) throws Exception {
        return getEntityManager().find(UploadedImage.class, id);
    }
    
    
}
