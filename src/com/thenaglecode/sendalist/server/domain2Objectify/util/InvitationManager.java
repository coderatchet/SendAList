package com.thenaglecode.sendalist.server.domain2Objectify.util;

import com.google.gson.JsonObject;
import com.thenaglecode.sendalist.server.domain2Objectify.unstoredObjects.Invitation;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 9/07/12
 * Time: 10:02 PM
 * <p/>
 * singleton class that stores the invitations
 */
public class InvitationManager {
    private long TTL = 1000 * 60 * 60 * 24 * 30; //30 days

    private static InvitationManager instance;
    private static boolean developerMode = false;
    Map<String, List<TimeStampedInvitation>> map = new HashMap<String, List<TimeStampedInvitation>>();
    private Set<TimeStampedInvitation> sortedQueue = new TreeSet<TimeStampedInvitation>();

    public static InvitationManager getInstance() {
        if (instance == null) {
            instance = new InvitationManager();
        }
        return instance;
    }

    public void setTTL(long ttl) {
        this.TTL = ttl;
    }

    public static void setDeveloperMode(boolean developerMode) {
        InvitationManager.developerMode = developerMode;
    }

    public int getCount() {
        return sortedQueue.size();
    }

    public int getMapCount() {
        return map.size();
    }

    /**
     * @param invitation the invitation to place in the map
     */
    public synchronized void add(@NotNull Invitation invitation) {
        cleanupOldInvitations();
        String emailTo = invitation.getEmailTo();
        TimeStampedInvitation timeStampedInvitation = null;
        List<TimeStampedInvitation> existingInvitations = map.get(emailTo);
        if (existingInvitations == null) {
            existingInvitations = new ArrayList<TimeStampedInvitation>();
            timeStampedInvitation = new TimeStampedInvitation(invitation);
            existingInvitations.add(timeStampedInvitation);
            map.put(emailTo, existingInvitations);
            sortedQueue.add(timeStampedInvitation);
        } else {
            for (TimeStampedInvitation wrapper : existingInvitations) {
                if (wrapper.invitation.equals(invitation)) {
                    timeStampedInvitation = wrapper;
                    break;
                }
            }
            if (timeStampedInvitation == null) { //if not found then add it
                timeStampedInvitation = new TimeStampedInvitation(invitation);
                existingInvitations.add(timeStampedInvitation);
                sortedQueue.add(timeStampedInvitation);
            } else { //if it is found, then reset it's time.
                sortedQueue.remove(timeStampedInvitation);
                timeStampedInvitation.resetTimestamp();
                sortedQueue.add(timeStampedInvitation);
            }
        }
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

        //remove them from the other map.
        for (TimeStampedInvitation timeStampedInvitation : thingsToDelete) {
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

    /**
     * this object enables a timestamp to be associated with the object.
     */
    private static class TimeStampedInvitation implements Comparable<TimeStampedInvitation> {
        private long timestamp;

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

        public Invitation getInvitation() {
            return invitation;
        }

        @Override
        public int compareTo(TimeStampedInvitation o) {
            if (timestamp < o.timestamp) return -1;
            else return 1;
        }
    }
}
