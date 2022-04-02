package com.cb.reconciliation.utils;

import org.json.JSONException;
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

    // todo: fix
    public static JSONObject toOrgJSON(org.json.simple.JSONObject simpleJsonObject) throws ParseException, JSONException {
        String data = simpleJsonObject.toJSONString();
//        JSONObject orgJSON = new JSONObject(data);
        System.out.println("Data");
        System.out.println(data);
        return new JSONObject(data);
    }

}
