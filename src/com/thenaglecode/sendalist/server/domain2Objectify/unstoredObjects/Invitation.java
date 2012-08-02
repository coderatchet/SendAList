package com.thenaglecode.sendalist.server.domain2Objectify.unstoredObjects;

import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 9/07/12
 * Time: 10:05 PM
 *
 * An invitation represents a user's request to another user to "see ({@link Type#View}), have a copy
 * ({@link Type#Copy}) or be able to edit({@link Type#Edit})" the list.
 */
public class Invitation {
    public static enum Type {View, Edit, Copy}

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

    /** the user who sent the request */
    private String emailFrom;
    /** the user the request needs a response from */
    private String emailTo;
    /** the id that the list concerns */
    private long taskListId;
    /** the type of request this is. */
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

    public Type getType() {
        return type;
    }

    public boolean equalsNotIncludingFromAndType(Invitation invitation) {
        return emailTo.equals(invitation.emailTo) && taskListId == invitation.taskListId;
    }

    public String toString() {
        return new StringBuilder().append(FIELD_FROM).append(": ").append(emailFrom)
                .append(FIELD_TO).append(": ").append(emailTo)
                .append(FIELD_ID).append(": ").append(taskListId)
                .append(FIELD_TYPE).append(": ").append(type.toString()).toString();
    }
}
