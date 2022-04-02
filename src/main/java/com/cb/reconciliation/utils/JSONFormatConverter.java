package com.cb.reconciliation.utils;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONFormatConverter {
    public static org.json.simple.JSONObject toSimpleJSON(JSONObject jsonObject) throws ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(jsonObject.toString());
        org.json.simple.JSONObject simpleJSONFormat = (org.json.simple.JSONObject) obj;
        return simpleJSONFormat;
    }

}
