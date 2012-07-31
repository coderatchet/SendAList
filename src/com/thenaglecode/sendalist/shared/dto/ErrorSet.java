package com.thenaglecode.sendalist.shared.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 31/07/12
 * Time: 10:00 PM
 */
public class ErrorSet {
        public int code;
        public String message;
        public String reason = null;

        public ErrorSet() {
        }

        public ErrorSet(int code, String message, String reason) {
            this.code = code;
            this.message = message;
            this.reason = reason;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public JsonObject toJson() {
            Gson gson = new GsonBuilder().create();
            return (JsonObject) gson.toJsonTree(this);
        }
}
