package com.thenaglecode.sendalist.server.domain2Objectify.unstoredObjects;

import com.thenaglecode.sendalist.server.domain2Objectify.interfaces.ToJson;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 9/07/12
 * Time: 10:05 PM
 * <p/>
 * An invitation represents a user's request to another user to "see ({@link Type#view}), have a copy
 * ({@link Type#copy}) or be able to edit({@link Type#edit})" the list.
 */
public class Invitation implements ToJson{
    public void upgradeToEdit() {
        this.type = Type.edit;
    }

    public static enum Type {view, edit, copy}

    public static final String FIELD_FROM = "from";
    public static final String FIELD_TO = "to";
    public static final String FIELD_ID = "id";
    public static final String FIELD_TYPE = "type";

    public Invitation(@NotNull String emailFrom, @NotNull String emailTo, long taskListId, @NotNull Type type) {
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.taskListId = taskListId;
        this.type = type;
    }

    /**
     * the user who sent the request
     */
    private String emailFrom;
    /**
     * the user the request needs a response from
     */
    private String emailTo;
    /**
     * the id that the list concerns
     */
    private long taskListId;
    /**
     * the type of request this is.
     */
    private Type type;

    public String getEmailFrom() {
        return emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public long getTaskListId() {
        return taskListId;
    }
    
    public void setTaskListId(long taskListId){
        this.taskListId = taskListId;
    }

    public Type getType() {
        return type;
    }

    public boolean equalsNotIncludingType(Invitation invitation) {
        return  emailFrom.equals(invitation.emailFrom)
                && emailTo.equals(invitation.emailTo)
                && taskListId == invitation.taskListId;
    }

    public String toString() {
        return new StringBuilder().append(FIELD_FROM).append(": ").append(emailFrom)
                .append(FIELD_TO).append(": ").append(emailTo)
                .append(FIELD_ID).append(": ").append(taskListId)
                .append(FIELD_TYPE).append(": ").append(type.toString()).toString();
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(FIELD_FROM, this.getEmailFrom());
            obj.put(FIELD_TO, this.getEmailTo());
            obj.put(FIELD_ID, this.getTaskListId());
            obj.put(FIELD_TYPE, this.getType());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }
}
