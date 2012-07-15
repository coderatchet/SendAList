package com.thenaglecode.sendalist.shared.dto;

import com.google.gwt.json.client.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: JELLYBEANS
 * Date: 6/8/12
 * Time: 5:03 PM
 */
public class GWTTask implements IsSerializable {

    public GWTTask() {
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    boolean done;
    String summary;

    public GWTTask(boolean done, String summary) {
        this.done = done;
        this.summary = summary;
    }

    @Nullable
    public static GWTTask fromJson(String jsonString){
        GWTTask task;
        Boolean done = null;
        String summary = null;
        JSONValue parsed = JSONParser.parseStrict(jsonString);
        JSONObject json = parsed.isObject();
        if(json.containsKey("done")){
            JSONBoolean doneBool = json.get("done").isBoolean();
            if(doneBool != null){
                done = doneBool.booleanValue();
            }
        }
        if(json.containsKey("summary")){
            JSONString summaryString = json.get("summary").isString();
            if(summaryString != null){
                summary = summaryString.stringValue();
            }
        }
        if(done != null && summary != null) return new GWTTask(done, summary);
        else return null;
    }
}
