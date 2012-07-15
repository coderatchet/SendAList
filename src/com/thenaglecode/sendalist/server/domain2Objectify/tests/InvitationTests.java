package com.thenaglecode.sendalist.server.domain2Objectify.tests;

import com.thenaglecode.sendalist.server.domain2Objectify.unstoredObjects.Invitation;
import com.thenaglecode.sendalist.server.domain2Objectify.util.InvitationManager;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 10/07/12
 * Time: 2:34 PM
 */
public class InvitationTests {
    public InvitationTests() {
    }

    @Test
    public void test1() {
        InvitationManager.setDeveloperMode(true);
        InvitationManager manager = InvitationManager.getInstance();
        System.out.println("Setting time to live to 2 seconds");
        manager.setTTL(1000); // 1 sec time to live.
        Invitation one = new Invitation("me", "you", 1, Invitation.Type.View);
        Invitation two = new Invitation("you", "me", 2, Invitation.Type.Copy);
        manager.add(one);
        manager.printState();
        manager.add(two);
        manager.printState();
        System.out.println("current invitations: ");
        List<Invitation> invitations = manager.getInvitations("me");
        System.out.println("me has invitations: ");
        for (Invitation invitation : invitations) {
            System.out.println("\t" + invitation.toString());
        }
        System.out.println("you has invitations: ");
        invitations = manager.getInvitations("you");
        for (Invitation invitation : invitations) {
            System.out.println("\t" + invitation.toString());
        }
        int waitSeconds = 4;
        System.out.print("\ngo to sleep for " + waitSeconds + " seconds");
        try {
            for (int i = 0; i < waitSeconds; i++) {
                Thread.sleep(1000);
                System.out.print('.');
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }

        System.out.println("\n\nPlacing a new invitation in should get rid of the other invitations...");
        manager.add(new Invitation("him", "her", 3, Invitation.Type.Edit));
        assertEquals(1, manager.getCount());
        assertEquals(1, manager.getMapCount());
        invitations = manager.getInvitations("me");
        assertTrue(invitations.isEmpty());
        System.out.println("'me' has no invites :)");
        invitations = manager.getInvitations("you");
        assertTrue(invitations.isEmpty());
        System.out.println("'you' has no invites :)");
        invitations = manager.getInvitations("her");
        assertTrue(!invitations.isEmpty());
        System.out.println("'her' has invitations:");
        for (Invitation invitation : invitations) {
            System.out.println("\t" + invitation.toString());
        }
    }
}
