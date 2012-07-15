package com.thenaglecode.sendalist.server.domain2Objectify.interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.thenaglecode.sendalist.server.domain2Objectify.SendAListDAO;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.TaskList;
import com.thenaglecode.sendalist.shared.OriginatorOfPersistentChange;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 11/07/12
 * Time: 4:01 PM
 */
public class RequestProcessor {

    public static final String Nop = "Nop";

    /**
     * processes a command from a request made to the server.
     * <p/>
     * convention is that the id is the id of the object, otherwise if the identifier is
     * "new" then we need to create a new object.
     *
     * @param tx      the json transaction with at least a c for the command.
     * @param context the context from which this transaction was sent
     * @return an error if something was a problem,
     *         the Nop string if nothing was changed and null if there was no problem.
     */
    public String processTransaction(@NotNull JsonObject tx, OriginatorOfPersistentChange context) {
        boolean isNew = false;
        Long id = -1l;

        JsonArray txs = tx.getAsJsonArray();
        String c = tx.get("c").getAsString(); //this represents the command, usually the object type.
        String i = tx.get("i").getAsString(); //this represents the id or the instruction to create a new object
        SendAListDAO dao = new SendAListDAO();
        if (c == null) return "Could not read the command";
        if (i == null) return "Could not read the id";
        if (i.equals("new")) isNew = true;

        if ("TASKLIST".equals(c)) {
            TaskList taskList;
            if (isNew) {
                //noinspection UnusedAssignment //todo delete this line when done
                taskList = new TaskList();
            } else {
                //noinspection UnusedAssignment //todo delete this line when done
                taskList = dao.findTaskList(id);
            }
            //todo finish tasklist processing
        } else if ("USER".equals(c)) {
            //todo finish user processing
        }
        return null;
    }

    /**
     * if the error is not null and not equal to "{@value #Nop}" then it returns true, otherwise it is not an error and
     * returns false.
     *
     * @param err the error message to check.
     * @return returns true if the string is an error or false if not.
     */
    public boolean returnedError(String err) {
        return err != null && !Nop.equals(err);
    }
}
