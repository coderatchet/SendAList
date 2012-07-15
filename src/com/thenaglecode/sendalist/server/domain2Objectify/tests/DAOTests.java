package com.thenaglecode.sendalist.server.domain2Objectify.tests;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.thenaglecode.sendalist.server.Globals;
import com.thenaglecode.sendalist.server.domain2Objectify.SendAListDAO;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.UserAccount;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 7/07/12
 * Time: 5:46 PM
 */
public class DAOTests {

    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    public DAOTests(){}

    @Test
    public void testWithRandomUsers(){
        SendAListDAO dao = new SendAListDAO();
        Globals.createAndPersistFakeUsers(5);
        System.out.println("Here we go");
        List<UserAccount> users = dao.getAllUsers();
        for(UserAccount user : users){
            System.out.println("\n" + user.getTaskListsAsString());
        }
    }
}
