package com.thenaglecode.sendalist.server.domain2Objectify.interfaces;

import org.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 9/07/12
 * Time: 9:58 PM
 * <p/>
 * put this on objects you wish to be serialised to Json.
 */
public interface ToJson {
    public JSONObject toJson();
}
