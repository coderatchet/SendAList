package com.thenaglecode.sendalist.server.domain2Objectify.entities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.googlecode.objectify.Key;
import com.thenaglecode.sendalist.server.domain2Objectify.SendAListDAO;
import com.thenaglecode.sendalist.server.domain2Objectify.interfaces.Processable;
import com.thenaglecode.sendalist.server.domain2Objectify.interfaces.RequestProcessor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Embedded;
import javax.persistence.Id;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 7/07/12
 * Time: 8:13 PM
 */
public class TaskList implements Processable {

    @Id
    public Long id;
    String summary;
    @Embedded
    private Set<Task> tasks = null;
    private Key<UserAccount> owner = null;

    public TaskList() {
    }

    public TaskList(long id) {
        this.id = id;
    }

    public long getId(){
        return id;
    }

    public TaskList setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public Key<UserAccount> getOwner() {
        return owner;
    }

    /**
     * returns the list of embedded tasks
     *
     * @return list of embedded tasks
     */
    @NotNull
    public Set<Task> getTasks() {
        if (tasks == null) {
            tasks = new TreeSet<Task>();
        }
        return tasks;
    }

    public TaskList addTask(@NotNull Task taskToAdd) {
        Set<Task> tasks = getTasks();
        boolean found = false;
        for (Task task : tasks) {
            if (task.equals(taskToAdd)) {
                found = true;
                break;
            }
        }
        if (!found) tasks.add(taskToAdd);
        return this;
    }

    public TaskList addTasks(@NotNull Task... tasks) {
        for (Task task : tasks) {
            addTask(task);
        }
        return this;
    }

    public TaskList deleteTask(long id) {
        Set<Task> tasks = getTasks();
        for (Task task : tasks) {
            if (task.getId() == id) {
                return deleteTask(task);
            }
        }
        return this;
    }

    public TaskList deleteTask(@NotNull Task task) {
        Set<Task> tasks = getTasks();
        if (tasks.contains(task)) {
            tasks.remove(task);
        }
        return this;
    }

    @NotNull
    public Key<TaskList> generateKey() {
        return new Key<TaskList>(owner, TaskList.class, this.id);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n'-->Summary: ").append(summary);
        Set<Task> tasks = getTasks();
        for (Task task : tasks) {
            sb.append("\n\t").append(task.getSummary());
            sb.append(" is ").append((task.getDone()) ? "done" : "not done");
        }
        return sb.toString();
    }

    public TaskList setOwner(@NotNull Key<UserAccount> userAccountKey) {
        owner = userAccountKey;
        return this;
    }

    public String toHtmlList() {
        StringBuilder sb = new StringBuilder();
        sb.append("<h3>-->Summary: ").append(summary).append("</h3>");
        Set<Task> tasks = getTasks();
        if (tasks.size() > 0) {
            sb.append("<ul>");
            for (Task task : tasks) {
                sb.append(task.toHtmlListElement());
            }
            sb.append("</ul>");
        } else {
            sb.append("<h4>no items!</h4>");
        }
        return sb.toString();
    }

    private final String VALUE_PARSE_PROBLEM = "problem translating the value";

    /**
     * {@inheritDoc}
     */
    @Override
    public String processTransaction(@NotNull JsonObject tx) {
        boolean changed = false;

        for (Map.Entry<String, JsonElement> entry : tx.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            boolean couldNotParse = false;
            String valueAsString = null;
            try {
                valueAsString = entry.getValue().getAsString();
            } catch (ClassCastException e) {
                couldNotParse = true;
            }

            if ("c".equals(key) || "i".equals(key)) {
                //do nothing
            } else if ("summary".equals(key)) {
                if (couldNotParse) return VALUE_PARSE_PROBLEM;
                else if (this.getSummary() == null || !this.getSummary().equals(valueAsString)) {
                    changed = true;
                    setSummary(valueAsString);
                }
            } else if ("owner".equals(key)) {
                SendAListDAO dao = new SendAListDAO();
                if (couldNotParse) return VALUE_PARSE_PROBLEM;
                Key existingKey = getOwner();
                UserAccount found = dao.findUser(valueAsString);
                if (found == null) return "User is not in the database: " + valueAsString;
                if (existingKey == null || !existingKey.getName().equals(valueAsString)) {
                    changed = true;
                    Key<UserAccount> newKey = new Key<UserAccount>(UserAccount.class, valueAsString);
                    setOwner(newKey);
                }
            } else if ("task".equals(key)) {
                boolean isNew = false;
                boolean isDelete = false;
                JsonObject obj = value.getAsJsonObject();
                if (obj.get("del") != null) {
                    isDelete = true;
                }
                String err;
                if (isNew) {
                    Task newTask = new Task();
                    err = newTask.processTransaction(obj);
                    if (RequestProcessor.returnedError(err)) {
                        return err;
                    } else if (newTask.isSafeToPersist()) {
                        changed = true;
                        this.addTask(newTask);
                    } else {
                        return "the new task was not safe to persist: " + obj.toString();
                    }
                } else {
                    JsonElement idElement = obj.get("i");
                    if (idElement == null) return "there was no id in the task flagged for deletion";
                    String id = idElement.getAsString();
                    long idNumber;
                    try {
                        idNumber = Long.valueOf(id);
                    } catch (NumberFormatException e) {
                        return "id for task to delete was not of type Long: " + id;
                    }
                    boolean found = false;
                    Iterator<Task> itr = this.tasks.iterator();
                    while (itr.hasNext() && !found) {
                        Task existingTask = itr.next();
                        if (existingTask.getId() == idNumber) {
                            found = true;
                            if (isDelete) {
                                changed = true;
                                tasks.remove(existingTask);
                            } else {
                                // adjust the copy to make sure the real object won't be changed if there is an error.
                                Task copy = existingTask.duplicate(false);
                                err = copy.processTransaction(obj);
                                if (RequestProcessor.returnedError(err)) {
                                    return err;
                                } else if (!existingTask.isSafeToPersist()) {
                                    return "the task does not have enough information: " + obj.toString();
                                } else {
                                    changed = true;
                                    existingTask.processTransaction(obj);
                                }
                            }
                        }
                    }
                    if (!found) return "the task set for " + ((isDelete) ? "deletion" : "updating")
                            + " was not found! id: " + idNumber;
                }
            } else {
                return "did not understand field for TaskList processing: " + key;
            }
        }

        if (!changed) return Processable.Nop;
        return null;
    }

    private void processDeleteTransaction() {
        SendAListDAO dao = new SendAListDAO();
        UserAccount owner = dao.findUser(this.getOwner().getName());
        owner.deleteTaskList(this.generateKey());
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSafeToPersist() {
        boolean isSafe = summary != null && owner != null;
        if (tasks != null) {
            for (Task task : tasks) {
                if (isSafe) isSafe = task.isSafeToPersist();
            }
        }
        return isSafe;
    }
}
