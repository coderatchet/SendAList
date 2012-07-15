package com.thenaglecode.sendalist.server.domain2Objectify.util;

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
        System.out.println("there are " + getCount() + " things in the sortedQueue");
        for (TimeStampedInvitation timeStampedInvitation : sortedQueue) {
            if (now - timeStampedInvitation.getTimestamp() > TTL) {
                thingsToDelete.add(timeStampedInvitation);
            } else break;
        }

        sortedQueue.removeAll(thingsToDelete);

        //remove them from the other map.
        System.out.println("there are " + thingsToDelete.size() + " things that are being deleted in cleanup");
        for (TimeStampedInvitation timeStampedInvitation : thingsToDelete) {
            List<TimeStampedInvitation> invitations = map.get(timeStampedInvitation.getInvitation().getEmailTo());
            invitations.remove(timeStampedInvitation);
            System.out.println("invitations are empty? " + invitations.isEmpty());
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
        System.out.println("map count: " + getMapCount());
        for (String email : map.keySet()) {
            for (TimeStampedInvitation tsi : map.get(email)) {
                System.out.println("\t" + tsi.invitation.toString());
            }
        }
        System.out.println("queue count: " + getCount());
        for (TimeStampedInvitation tsi : sortedQueue) {
            System.out.println("\t" + tsi.invitation.toString());
        }
    }

    /**
     * this object enables a timestamp to be associated with the object.
     */
    private static class TimeStampedInvitation implements Comparable<TimeStampedInvitation> {
        private long timestamp;

        public Invitation invitation;

        public TimeStampedInvitation(Invitation invitation) {
            timestamp = System.currentTimeMillis();
            System.out.println("new: " + timestamp);
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
