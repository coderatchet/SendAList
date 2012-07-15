package com.thenaglecode.sendalist.server.domain2Objectify.entities;

import com.google.gson.JsonObject;
import com.thenaglecode.sendalist.server.domain2Objectify.interfaces.Processable;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.persistence.Id;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 7/07/12
 * Time: 8:14 PM
 * <p/>
 * Tasks must be created in an existing tasklist
 */
public class Task implements Processable, Comparable<Task> {

    @Id
    Long id;
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

    public Long getId() {
        return id;
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
     * {@inheritDoc}
     */
    public String processTransaction(JsonObject tx) {
        return null; //todo implement
    }

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
}
