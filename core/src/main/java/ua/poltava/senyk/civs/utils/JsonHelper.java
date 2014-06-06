/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.poltava.senyk.civs.utils;

import net.sf.json.JSONObject;

/**
 * Helper class to work with JSON data
 * @author Msenyk
 */
public class JsonHelper {
    
    /**
     * Check into JSON object for required String field by name
     * @param fieldName Name of field
     * @param json JSON object
     * @return True if field presents and isn't empty
     */
    public static boolean hasRequiredStringField(String fieldName, JSONObject json) {
        if ( json.containsKey(fieldName) ) {
            String value = json.getString(fieldName).trim();
            return ( value.length() > 0 );
        }
        return false;
    }
    
}
