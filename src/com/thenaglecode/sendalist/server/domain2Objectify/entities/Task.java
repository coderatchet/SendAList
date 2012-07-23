package com.thenaglecode.sendalist.server.domain2Objectify.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.thenaglecode.sendalist.server.domain2Objectify.interfaces.Processable;
import org.jetbrains.annotations.NotNull;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 7/07/12
 * Time: 8:14 PM
 * <p/>
 * Tasks must be created in an existing tasklist
 */
public class Task implements Comparable<Task> {

    private boolean done;
    private long created;
    private String summary = null;

    public Task(String summary) {
        this();
        this.summary = summary;
        this.done = false;
    }

    public Task() {
        created = System.currentTimeMillis();
    }

    public Task setDone(boolean done) {
        this.done = done;
        return this;
    }

    public boolean getDone() {
        return done;
    }

    public long getCreated() {
        return created;
    }

    public String getCreatedRfc3339() {
        //todo implement with jodaTime package
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        return "Not yet implemented";
    }

    public Task setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(getDone() ? " " : "X").append("] ");
        sb.append(getSummary());
        return sb.toString();
    }

    public String toHtmlListElement() {
        return "<li>" + toString() + "</li>";
    }

    /**
     * this function will process an array that represents the task.
     *
     * ["summary string",boolean-for-done]
     */
    public String processTransaction(JsonArray tx) {
        boolean changed = false;



        for (Map.Entry<String, JsonElement> entry : tx.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if(key == null) return "key was null";
            if(value == null) return "value was null";

            String valueAsString = null;
            if("c".equals(key) || "i".equals(key)){
                //do nothing
            } else if("summary".equals(key)){
                valueAsString = value.getAsString();
                if(valueAsString == null)
                    return "could not parse summary value: " + value.toString();
                if(this.getSummary() == null || !this.getSummary().equals(valueAsString)){
                    changed = true;
                    this.setSummary(valueAsString);
                }
            } else if("done".equals(key)){
                boolean newDone = value.getAsBoolean();
                if(newDone != this.getDone()){
                    changed = true;
                    this.setDone(newDone);
                }
            } else {
                return "unknown field in task transaction: " + key;
            }
        }

        return (changed) ? null : Processable.Nop;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSafeToPersist() {
        return summary != null;
    }

    @Override
    /** {@inheritDoc} */
    public int compareTo(Task o) {
        int compare = Long.valueOf(created).compareTo(o.getCreated());
        boolean safeToPersist = isSafeToPersist();
        if (compare != 0 || !safeToPersist) return compare;
        assert summary != null;
        compare = summary.compareTo(o.getSummary());
        if (compare != 0) return compare;
        compare = Boolean.valueOf(done).compareTo(o.getDone());
        return compare;
    }

    /**
     * this function will return an exact duplicate of this task unless you specify that you want a duplicate with a new
     * id (and creation time of now).
     */
    @NotNull
    public Task duplicate(boolean withNewCreationTime) {
        Task copy = new Task();
        if (withNewCreationTime) {
            copy.created = System.currentTimeMillis();
        } else {
            copy.created = this.created;
        }
        copy.setSummary(this.summary);
        copy.setDone(this.done);
        return copy;
    }
}
