package com.thenaglecode.sendalist.server;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.JsonObject;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import com.thenaglecode.sendalist.server.servlets.TransactionServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 21/07/12
 * Time: 10:18 PM
 */
public class ServletTests {

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

    private static JsonObject obj;

    static {
        obj = new JsonObject();
        obj.addProperty("c", "USER");
        obj.addProperty("i", "new");
        obj.addProperty("email", "jared@buzzhives.com");
    }

    @Test
    public void testTransactionServlet(){
        //register the servlets
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("api", TransactionServlet.class.getName());

        //initialise the 'client'
        ServletUnitClient sc = sr.newClient();
        WebRequest request = new PostMethodWebRequest("localhost:8080/api");
        request.setHeaderField("Content-Type", "application/json");

    }
}
