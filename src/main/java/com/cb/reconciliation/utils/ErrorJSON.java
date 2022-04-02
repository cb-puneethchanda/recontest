package com.cb.reconciliation.utils;

import org.json.JSONException;
import org.json.simple.JSONObject;

public class ErrorJSON {
    private String error;
    private String message;

    @Override
    public String toString() {
        return "ErrorJSON{" +
                "error='" + error + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorJSON(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("error", this.error);
        json.put("message", this.message);
        return json;
    }
}
