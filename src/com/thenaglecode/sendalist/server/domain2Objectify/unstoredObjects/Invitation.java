package com.thenaglecode.sendalist.server.domain2Objectify.unstoredObjects;

import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 9/07/12
 * Time: 10:05 PM
 */
public class Invitation {
    public static enum Type {View, Edit, Copy}

    public Invitation(@NotNull String emailFrom, @NotNull String emailTo, long taskListId, @NotNull Type type) {
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.taskListId = taskListId;
        this.type = type;
    }

    private String emailFrom;
    private String emailTo;
    private long taskListId;
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
        return new StringBuilder().append("from: ").append(emailFrom)
                .append(" to: ").append(emailTo)
                .append(" taskListId: ").append(taskListId)
                .append(" Type:").append(type.toString()).toString();
    }
}
