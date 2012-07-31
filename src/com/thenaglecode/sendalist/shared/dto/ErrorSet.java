package com.thenaglecode.sendalist.shared.dto;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 31/07/12
 * Time: 10:00 PM
 */
public class ErrorSet extends JSONObject{
    public static final ErrorSet UNKNOWN_FORMAT_ERROR = new ErrorSet(400, "unknown request format!",
            "the request was not in the correct format, please consult the api documentation for the desired formats");
    public final String CODE = "code";
    public final String MESSAGE = "message";
    public final String REASON = "reason";

    public ErrorSet() {
    }

    public ErrorSet(int code, String message, String reason) {
        setCode(code);
        setMessage(message);
        setReason(reason);
    }

    public void setCode(int code) {
        try {
            this.put(CODE, code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String message) {
        try{
            this.put(MESSAGE, message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setReason(String reason) {
        try{
            this.put(REASON, reason);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
