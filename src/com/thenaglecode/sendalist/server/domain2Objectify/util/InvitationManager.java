package com.thenaglecode.sendalist.server.domain2Objectify.util;

import com.google.gson.JsonObject;
import com.googlecode.objectify.Key;
import com.thenaglecode.sendalist.server.domain2Objectify.SendAListDAO;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.TaskList;
import com.thenaglecode.sendalist.server.domain2Objectify.unstoredObjects.Invitation;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 9/07/12
 * Time: 10:02 PM
 * <p/>
 * singleton class that stores the invitations.
 * <p/>
 * <p>
 * Each invitation is one of three types: View, Copy or Edit. if an invitation exists for a particular user for a
 * particular item list, then it's status can be refreshed or overwritten if another invitation is made for that
 * person. Edit can overwrite View invitations (since a user can also view a list they are able to edit) and copy
 * invitations are new invitations all together.
 * </p>
 * <p/>
 * <p>
 * A note about copy invitations is that sending the invitation out to a person to receive a copy will also create
 * the list in preempt in order to increase the efficiency of acceptances.
 * </p>
 *
 * @see Invitation
 */
public class InvitationManager {
    private long TTL = 1000 * 60 * 60 * 24 * 30; //30 days

    private static InvitationManager instance;
    private static boolean developerMode = false;
    Map<String, List<TimeStampedInvitation>> map = new HashMap<String, List<TimeStampedInvitation>>();
    private Set<TimeStampedInvitation> sortedQueue = new TreeSet<TimeStampedInvitation>();
    private Map<Long, JSONObject> copies = new HashMap<Long, JSONObject>();

    public static InvitationManager getInstance() {
        if (instance == null) {
            instance = new InvitationManager();
        }
        return instance;
    }

    public void setTTL(long ttl) {
        this.TTL = ttl;
    }

    /**
     * sets whether developer output should be shown or not
     *
     * @param developerMode whether the output should be shown for development purposes or not
     */
    public static void setDeveloperMode(boolean developerMode) {
        InvitationManager.developerMode = developerMode;
    }

    /**
     * @return how many invitations are in the system
     */
    public int getCount() {
        return sortedQueue.size();
    }

    /**
     *
     * */
    public int getMapCount() {
        return map.size();
    }

    /**
     * this functions adds the invitation to the map of invitations.
     *
     * @param invitation the invitation to place in the map
     * @return an error message if there is one.
     */
    public synchronized String add(@NotNull Invitation invitation) {
        cleanupOldInvitations();
        String emailTo = invitation.getEmailTo();
        TimeStampedInvitation timeStampedInvitation = null;
        long originalTaskListId = invitation.getTaskListId();

        //check if a copy
        if (Invitation.Type.Copy.equals(invitation.getType())) {
            // does Does a copy of this list already exist for the to user?
            // (search the id for a name and id pair that match the to field)
            JSONObject json = copies.get(invitation.getTaskListId());
            try {
                if (json == null || json.get(emailTo) == null) {
                    //copy the list
                    SendAListDAO dao = new SendAListDAO();
                    TaskList originalList = dao.findTaskList(invitation.getTaskListId());
                    if (originalList == null) return "could not find corresponding list in database with id of ["
                            + invitation.getTaskListId() + "]";
                    TaskList duplicateTaskList = originalList.duplicate(true);
                    //store it in the database
                    Key<TaskList> key = dao.saveTaskList(duplicateTaskList);
                    if (key == null) return "there was a problem storing the duplicate task list in the database";
                    //add it to the copies
                    putIdInCopies(originalList.getId(), emailTo, key.getId());
                    invitation.setTaskListId(key.getId());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //register the invitation
        List<TimeStampedInvitation> existingInvitations = map.get(emailTo);
        if (existingInvitations == null) {
            //just add it
            existingInvitations = new ArrayList<TimeStampedInvitation>();
            timeStampedInvitation = new TimeStampedInvitation(invitation);
            timeStampedInvitation.setOriginalTaskListId(originalTaskListId);
            existingInvitations.add(timeStampedInvitation);
            map.put(emailTo, existingInvitations);
            sortedQueue.add(timeStampedInvitation);
        } else {
            boolean isUpgrade = false;
            for (TimeStampedInvitation wrapper : existingInvitations) {
                if (wrapper.invitation.equals(invitation)) {
                    timeStampedInvitation = wrapper;
                    break;
                } else if (wrapper.invitation.equalsNotIncludingType(invitation)
                        && wrapper.invitation.getType() == Invitation.Type.View
                        && invitation.getType().equals(Invitation.Type.Edit)) {
                    //if they are equal
                    isUpgrade = true;
                    timeStampedInvitation = wrapper;
                }
            }
            if (timeStampedInvitation == null && !isUpgrade) { //if not found then add it
                timeStampedInvitation = new TimeStampedInvitation(invitation);
                existingInvitations.add(timeStampedInvitation);
                sortedQueue.add(timeStampedInvitation);
            } else { //if it is found, then reset it's time.
                sortedQueue.remove(timeStampedInvitation);
                timeStampedInvitation.resetTimestamp();
                if (isUpgrade) {
                    timeStampedInvitation.invitation.upgradeToEdit();
                }
                sortedQueue.add(timeStampedInvitation);
            }
        }
        return null;
    }

    /**
     * called inside a synchronized function
     */
    private void cleanupOldInvitations() {
        long now = System.currentTimeMillis();
        List<TimeStampedInvitation> thingsToDelete = new ArrayList<TimeStampedInvitation>();
        for (TimeStampedInvitation timeStampedInvitation : sortedQueue) {
            if (now - timeStampedInvitation.getTimestamp() > TTL) {
                thingsToDelete.add(timeStampedInvitation);
            } else break;
        }

        sortedQueue.removeAll(thingsToDelete);

        //remove them from the other maps.
        for (TimeStampedInvitation timeStampedInvitation : thingsToDelete) {
            if (Invitation.Type.Copy.equals(timeStampedInvitation.invitation.getType())) {
                //remove from the copies map.
                JSONObject json = copies.get(timeStampedInvitation.originalTaskListId);
                if (json != null) {
                    //get the id of the list it created
                    long idToDelete = (Long) json.remove(timeStampedInvitation.invitation.getEmailTo());
                    SendAListDAO dao = new SendAListDAO();
                    TaskList found = dao.findTaskList(idToDelete);
                    // the object should never have an owner if it is still in this list, but regardless,
                    // we should check anyway lest we delete someones list.
                    if(found != null && found.getOwner() != null) dao.deleteTaskList(idToDelete);
                    if (json.length() == 0) copies.remove(timeStampedInvitation.originalTaskListId);
                    else copies.put(timeStampedInvitation.originalTaskListId, json);
                }
            }
            List<TimeStampedInvitation> invitations = map.get(timeStampedInvitation.getInvitation().getEmailTo());
            invitations.remove(timeStampedInvitation);
            if (invitations.isEmpty()) {
                map.remove(timeStampedInvitation.invitation.getEmailTo());
            }
        }
    }

    @NotNull
    public synchronized List<Invitation> getInvitations(@NotNull String email) {
        List<Invitation> invitations = new ArrayList<Invitation>();
        List<TimeStampedInvitation> timeStampedInvitations = map.get(email);
        if (timeStampedInvitations != null)
            for (TimeStampedInvitation timeStampedInvitation : timeStampedInvitations) {
                invitations.add(timeStampedInvitation.invitation);
            }
        return invitations;
    }

    /**
     * prints the current state of the map
     */
    public void printState() {
        for (String email : map.keySet()) {
            for (TimeStampedInvitation tsi : map.get(email)) {
                System.out.println("\t" + tsi.invitation.toString());
            }
        }
        for (TimeStampedInvitation tsi : sortedQueue) {
            System.out.println("\t" + tsi.invitation.toString());
        }
    }

    public static String registerInvitation(JsonObject tx) {
        //check for all required fields.
        if (!tx.has(Invitation.FIELD_FROM) || !tx.get(Invitation.FIELD_FROM).isJsonPrimitive() || !tx.get(Invitation.FIELD_FROM).getAsJsonPrimitive().isString()) {
            return "problem reading field \"" + Invitation.FIELD_FROM + "\"";
        }
        if (!tx.has(Invitation.FIELD_TO) || !tx.get(Invitation.FIELD_TO).isJsonPrimitive() || !tx.get(Invitation.FIELD_TO).getAsJsonPrimitive().isString()) {
            return "problem reading field \"" + Invitation.FIELD_TO + "\"";
        }
        if (!tx.has(Invitation.FIELD_TYPE) || !tx.get(Invitation.FIELD_TYPE).isJsonPrimitive() || !tx.get(Invitation.FIELD_TYPE).getAsJsonPrimitive().isString()
                || Invitation.Type.valueOf(tx.get(Invitation.FIELD_TYPE).getAsString()) == null) {
            return "problem reading field \"" + Invitation.FIELD_TYPE + "\"";
        }
        if (!tx.has(Invitation.FIELD_ID) || !tx.get(Invitation.FIELD_ID).isJsonPrimitive() || !tx.get(Invitation.FIELD_ID).getAsJsonPrimitive().isNumber()) {
            return "problem reading field \"" + Invitation.FIELD_ID + "\"";
        }

        String from = tx.get(Invitation.FIELD_FROM).getAsString();
        String to = tx.get(Invitation.FIELD_TO).getAsString();
        long id = tx.get(Invitation.FIELD_ID).getAsLong();
        Invitation.Type type;
        try {
            type = Invitation.Type.valueOf(tx.get(Invitation.FIELD_TYPE).getAsString());
        } catch (IllegalArgumentException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("value for field ").append(Invitation.FIELD_TYPE)
                    .append(" was not a valid field! valid fields include: [");
            boolean first = true;
            for (Invitation.Type aType : Invitation.Type.values()) {
                if (first) {
                    first = false;
                    sb.append(aType.name());
                } else {
                    sb.append(',').append(aType.name());
                }
            }
            sb.append("]");
            return sb.toString();
        }

        Invitation newInvitation = new Invitation(from, to, id, type);
        getInstance().add(newInvitation);
        return null;
    }

    /* copy operations */

    /**
     * this function adds the key value pair of
     *
     * @param id      the id of the list that was copied
     * @param toEmail the email of the user this
     * @param copyId  the id of the newly duplicated list
     */
    private void putIdInCopies(long id, @NotNull String toEmail, long copyId) {
        JSONObject obj = copies.get(id);

        if (obj == null) {
            obj = new JSONObject();
            try {
                obj.put(toEmail, copyId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            copies.put(id, obj);
        }

        try {
            if (!obj.has(toEmail)) {
                obj.put(toEmail, copyId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * this object enables a timestamp to be associated with the object.
     */
    private static class TimeStampedInvitation implements Comparable<TimeStampedInvitation> {
        private long timestamp;
        public long originalTaskListId = -1;

        @NotNull
        public Invitation invitation;

        public TimeStampedInvitation(Invitation invitation) {
            timestamp = System.currentTimeMillis();
            this.invitation = invitation;
        }

        public void resetTimestamp() {
            timestamp = System.currentTimeMillis();
        }

        public long getTimestamp() {
            return timestamp;
        }

        @NotNull
        public Invitation getInvitation() {
            return invitation;
        }

        public void setOriginalTaskListId(long originalTaskListId) {
            this.originalTaskListId = originalTaskListId;
        }

        @Override
        public int compareTo(TimeStampedInvitation o) {
            if (timestamp < o.timestamp) return -1;
            else return 1;
        }
    }
}
