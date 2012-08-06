package com.thenaglecode.sendalist.server.domain2Objectify.interfaces;

import com.google.gson.JsonObject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Query;
import com.thenaglecode.sendalist.server.domain2Objectify.SendAListDAO;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.TaskList;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.UserAccount;
import com.thenaglecode.sendalist.server.domain2Objectify.util.InvitationManager;
import com.thenaglecode.sendalist.shared.OriginatorOfPersistentChange;
import com.thenaglecode.sendalist.shared.dto.ErrorSet;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 11/07/12
 * Time: 4:01 PM
 */
public class RequestProcessor {

    /**
     * processes a command from a request made to the server.
     * <p/>
     * convention is that the id is the id of the object, otherwise if the identifier is
     * "new" then we need to create a new object.
     * <p/>
     * <p>
     * <b>Object Types:</b>
     * <ul>
     * <li>USER - see {@link UserAccount}</li>
     * <li>LIST - see {@link TaskList}</li>
     * </ul>
     * </p>
     *
     * @param tx      the json transaction with at least a c for the command.
     * @param context the context from which this transaction was sent
     * @return a json object representing the new or updated resource, a json object stating that the object was deleted
     *         or an error object stating that there was an error
     */
    public JSONObject processTransaction(@NotNull JsonObject tx, OriginatorOfPersistentChange context) {
        boolean isNew = false;
        Long id = -1l;

        String c = tx.get(Processable.FIELD_TYPE).getAsString(); //this represents the command, usually the object type
        String i = null; //this represents the id or the instruction to create a new object
        if (!c.equals("INV"))
            i = tx.get(Processable.FIELD_ID).getAsString();
        SendAListDAO dao = new SendAListDAO();
        if (c == null) return getError("Could not read the command");
        if (!c.equals("INV") && i == null) return getError("Could not read the id");
        if (!c.equals("INV") && i.equals("new")) isNew = true;

        String err = null;
        if ("LIST".equals(c)) {
            TaskList taskList;
            if (isNew) taskList = new TaskList();
            else {
                try {
                    id = Long.valueOf(i);
                } catch (NumberFormatException e) {
                    return getError("could not parse TaskList id as long: " + i);
                }
                taskList = dao.findTaskList(id);
                if (taskList == null) {
                    return getError("Could not find TaskList with id: " + id);
                }
            }

            if (tx.get("del") == null) {
                if (tx.get("summary") != null) { //make sure we aren't naming a list after another one of our lists
                    Query<TaskList> q = dao.ofy().query(TaskList.class);
                    q.filter("summary", tx.get("summary").getAsString());
                    Iterator itr = q.iterator();
                    if (itr.hasNext()) {
                        return getError("task list already exists! could not create new list named: "
                                + tx.get("summary").getAsString());
                    }
                }

                err = taskList.processTransaction(tx);
                if (!returnedError(err)) {
                    if (!Processable.Nop.equals(err)) {
                        if (!taskList.isSafeToPersist()) {
                            return getError("Task list did not have enough information to save correctly");
                        }
                        dao.saveTaskList(taskList);
                        return taskList.toJson();
                    } else {
                        return getNotModified(c, i);
                    }
                } else {
                    return getError(err);
                }
            } else {
                //delete the task list
                UserAccount owner = dao.findUser(taskList.getOwner().getName());
                if (owner == null) {
                    return getError(
                            "could not find user with email: " + taskList.getOwner().getName());
                }

                List<Key<TaskList>> existingLists = owner.getTaskLists();
                Iterator<Key<TaskList>> itr = existingLists.iterator();
                Key<TaskList> foundList = null;
                while (foundList == null && itr.hasNext()) {
                    Key<TaskList> existingTaskList = itr.next();
                    if (existingTaskList.getId() == id) {
                        foundList = existingTaskList;
                    }
                }

                if (foundList != null) {
                    existingLists.remove(foundList);
                    dao.saveUser(owner);
                }

                dao.deleteTaskList(id);
                return getDeleteSuccess(c, i);
            }
        } else if ("USER".equals(c)) {
            UserAccount userAccount = dao.findOrCreateUser(i, true);
            err = userAccount.processTransaction(tx);

            if (!returnedError(err)) {
                if (!Processable.Nop.equals(err)) {
                    if (!userAccount.isSafeToPersist()) {
                        return getError(
                                "Task list did not have enough information to save correctly");
                    }
                    dao.saveUser(userAccount);
                    return userAccount.toJson();
                } else {
                    return getNotModified(c, i);
                }
            } else if (returnedError(err)) return getError(err);
        } else if ("INV".equals(c)) {
            //todo check if the user sending this is the one mentioned in "from" field of transaction
            err = InvitationManager.getInstance().registerInvitation(tx);
            if (!returnedError(err)) {
                if (!Processable.Nop.equals(err)) {
                    return getInvitationSuccess();
                }
                //todo cleanup
            }
        } else return getError("did not understand request type");
        return getError("unknown error occured in RequestProcessor");
    }

    /**
     * if the error is not null and not equal to "{@value Processable#Nop}" then it returns true, otherwise it is not an error and
     * returns false.
     *
     * @param err the error message to check.
     * @return returns true if the string is an error or false if not.
     */
    public static boolean returnedError(String err) {
        return err != null && !Processable.Nop.equals(err);
    }

    private JSONObject getInvitationSuccess() {
        return new ErrorSet(200, "success", "goodo");
    }

    private JSONObject getDeleteSuccess(@NotNull String type, @NotNull String id) {
        return new ErrorSet(200, "success", "successfully deleted entity: [" + type + ":" + id + "]");
    }

    private JSONObject getError(@NotNull String err) {
        return new ErrorSet(400, "Bad Request", err);
    }

    private JSONObject getNotModified(@NotNull String type, @NotNull String id) {
        return new ErrorSet(304, "Not Modified", "the transaction did nothing to [" + type + ":" + id + "]");
    }
}
