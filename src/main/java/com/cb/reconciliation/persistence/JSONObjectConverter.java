package com.cb.reconciliation.persistence;

import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class JSONObjectConverter implements AttributeConverter<JSONObject, String> {
    @Override
    public String convertToDatabaseColumn(JSONObject jsonData) {
        String json;
        try{
            json = jsonData.toString();
        }
        catch (NullPointerException ex)
        {
            // Error Handling
            json = "";
        }
        return json;
    }

    @Override
    public JSONObject convertToEntityAttribute(String jsonDataAsJson) {
        JSONObject jsonData;
        try {
            jsonData = new JSONObject(jsonDataAsJson);
        } catch (JSONException ex) {
            // Error Handling
            jsonData = null;
        }
        return jsonData;
    }
}
