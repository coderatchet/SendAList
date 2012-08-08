package com.thenaglecode.sendalist.server.domain2Objectify.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.googlecode.objectify.Key;
import com.thenaglecode.sendalist.server.domain2Objectify.SendAListDAO;
import com.thenaglecode.sendalist.server.domain2Objectify.interfaces.Processable;
import com.thenaglecode.sendalist.server.domain2Objectify.interfaces.RequestProcessor;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.Embedded;
import javax.persistence.Id;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 7/07/12
 * Time: 8:13 PM
 */
public class TaskList implements Processable {
    public static final String FIELD_SUMMARY = "summary"; //writable
    public static final String FIELD_TASKS = "tasks"; //read-only
    public static final String FIELD_OWNER = "owner"; //read-only
    public static final String FIELD_DEL_TASK = "deltask"; //used to delete a task
    public static final String FIELD_RENAME_TASK = "renametask"; //used to rename a task
    public static final String FIELD_COUNT = "num";

    @Id
    public Long id;
    String summary;
    @Embedded
    private List<Task> tasks = null;
    private Key<UserAccount> owner = null;
    private long created;

    public TaskList() {
        created = System.currentTimeMillis();
    }

    public TaskList(long id) {
        this();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    /**
     * set the summary/title of this task list
     *
     * @param summary the new summary for this task list.
     * @return this TaskList, Daisy chain style
     */
    public TaskList setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    /**
     * return the summary/title of this task list
     *
     * @return the summary/title of this task list
     */
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
    public List<Task> getTasks() {
        if (tasks == null) {
            tasks = new ArrayList<Task>();
        }
        return tasks;
    }

    public TaskList addTask(@NotNull Task taskToAdd) {
        List<Task> tasks = getTasks();
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

    public TaskList deleteTask(@NotNull String taskToDelete) {
        List<Task> tasks = getTasks();
        for (Task task : tasks) {
            if (task.getSummary().equals(taskToDelete)) {
                return deleteTask(task);
            }
        }
        return this;
    }

    public TaskList deleteTask(@NotNull Task task) {
        List<Task> tasks = getTasks();
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
        List<Task> tasks = getTasks();
        for (Task task : tasks) {
            sb.append("\n\t").append(task);
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
        List<Task> tasks = getTasks();
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
            boolean unsupported = false;
            String valueAsString = null;
            try {
                if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isString())
                    valueAsString = value.getAsString();
            } catch (ClassCastException e) {
                couldNotParse = true;
            } catch (UnsupportedOperationException e) {
                unsupported = true;
            }

            String VALUE_PARSE_PROBLEM = "problem translating the value";
            if (FIELD_TYPE.equals(key) || FIELD_ID.equals(key)) {
                //do nothing
            } else if (FIELD_SUMMARY.equals(key)) {
                if (couldNotParse) return VALUE_PARSE_PROBLEM;
                else if (this.getSummary() == null || !this.getSummary().equals(valueAsString)) {
                    changed = true;
                    setSummary(valueAsString);
                }
            } else if (FIELD_OWNER.equals(key)) {
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
            } else if (FIELD_TASKS.equals(key)) {
                if (!value.isJsonArray()) return "incorrect object: (" + value.toString()
                        + ") correct format: \"[summary(req),[done](opt),[photourl](opt)]\"";

                JsonArray rootArray = value.getAsJsonArray();
                for (JsonElement rootElement : rootArray) {
                    if (!rootElement.isJsonObject())
                        return "could not understand tasks object: " + rootElement.toString();
                    JsonObject taskObject = rootElement.getAsJsonObject();
                    List<Task> existingTasks = getTasks();
                    Task modTask = null;
                    boolean newTask = false;
                    if (!taskObject.has(Task.FIELD_SUMMARY)) {
                        return "task did not have the required " + FIELD_SUMMARY + " field: " + taskObject.toString();
                    } else if (!taskObject.get(FIELD_SUMMARY).isJsonPrimitive() &&
                            !taskObject.get(FIELD_SUMMARY).getAsJsonPrimitive().isString()) {
                        return FIELD_SUMMARY + " value is not a string: " + taskObject.toString();
                    } else {
                        //parse summary
                        String summary = taskObject.get(FIELD_SUMMARY).getAsString();
                        for (Task task : existingTasks) {
                            if (task.getSummary().equals(summary)) {
                                modTask = task;
                            }
                        }
                        if (modTask == null) {
                            changed = true;
                            newTask = true;
                            modTask = new Task(summary);
                        }
                    }
                    String err = modTask.processTransaction(taskObject);
                    if (RequestProcessor.returnedError(err)) {
                        return err;
                    } else {
                        if (!modTask.isSafeToPersist()) return "the task was missing some required fields: "
                                + taskObject.toString();
                        if (err == null || newTask) {
                            getTasks().add(modTask);
                        }
                    }
                }
            } else if (FIELD_DEL_TASK.equals(key)) {
                if (unsupported) return "could not support this value type: " + value.toString();
                if (valueAsString == null) return "value called for field " + FIELD_DEL_TASK + " is null!";

                Task toDelete = null;
                Iterator<Task> itr = this.getTasks().iterator();
                while (toDelete == null && itr.hasNext()) {
                    Task existingTask = itr.next();
                    if (valueAsString.equalsIgnoreCase(existingTask.getSummary())) {
                        toDelete = existingTask;
                    }
                }

                if (toDelete != null) {
                    changed = true;
                    getTasks().remove(toDelete);
                }
            } else if (FIELD_RENAME_TASK.equals(key)) {
                // <oldname>,<newname>
                if (unsupported) return "the value type is unsupported";
                if (valueAsString == null) return "the value for field \"renameTask\" is null!";

                String[] terms = valueAsString.split(",");
                if (terms.length != 2) return "incorrect paramaters: (" + valueAsString
                        + ") correct format is <oldname>,<newname>";

                String oldName = terms[0];
                String newSummary = terms[1];

                if (newSummary.isEmpty()) return "cannot rename task to empty string";

                List<Task> existingTasks = getTasks();
                Iterator<Task> itr = existingTasks.iterator();
                Task found = null;
                while (found == null && itr.hasNext()) {
                    Task existingTask = itr.next();
                    if (oldName.equals(existingTask.getSummary())) {
                        found = existingTask;
                    }
                }

                if (found != null) {
                    //check if new name already exists
                    itr = existingTasks.iterator();
                    Task foundTwo = null;
                    while (foundTwo == null && itr.hasNext()) {
                        Task existingTask = itr.next();
                        if (newSummary.equals(existingTask.getSummary())) {
                            foundTwo = existingTask;
                        }
                    }
                    if (foundTwo != null) return "task with this name already exists, cannot rename task: \""
                            + newSummary + "\"";
                    else {
                        changed = true;
                        found.setSummary(newSummary);
                    }
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
        if (owner != null) {
            owner.deleteTaskList(this.generateKey());
        }
    }

    public TaskList duplicate(boolean withNewCreationTime) {
        TaskList duplicateTaskList = new TaskList();
        if (!withNewCreationTime) duplicateTaskList.setCreated(getCreated());
        duplicateTaskList.setSummary(getSummary());
        List<Task> tasks = getTasks();
        for (Task task : tasks) {
            duplicateTaskList.addTask(task.duplicate(withNewCreationTime));
        }
        return duplicateTaskList;
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

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(FIELD_ID, String.valueOf(this.getId()));
            if (getSummary() != null) obj.put(FIELD_SUMMARY, this.getSummary());
            if (getOwner() != null) obj.put(FIELD_OWNER, this.getOwner().getName());
            if (getTasks().size() > 0) {
                JSONArray taskArray = new JSONArray();
                for (Task task : getTasks()) {
                    taskArray.put(task.toJson());
                }
                obj.put(FIELD_TASKS, taskArray);
            }
            //put tasks in array
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
