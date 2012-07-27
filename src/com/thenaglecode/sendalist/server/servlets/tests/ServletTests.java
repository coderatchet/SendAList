package com.thenaglecode.sendalist.server.servlets.tests;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import com.thenaglecode.sendalist.server.servlets.TransactionServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 21/07/12
 * Time: 10:18 PM
 */
public class ServletTests {

    private final String INPUT_FOLDER = "C:\\SendAList\\src\\com\\thenaglecode\\sendalist\\server\\servlets\\tests\\input\\";

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

    @Test
    public void testTransactionServlet(){
        //register the servlets
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("api", TransactionServlet.class.getName());

        //initialise the 'client'
        ServletUnitClient sc = sr.newClient();
        InputStream stream;

        try {
            stream = new FileInputStream(INPUT_FOLDER + "adduser.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
            return;
        }

        WebRequest request = new PostMethodWebRequest("http://locahost:8080/api", stream, "application/json");
        WebResponse response;
        try {
            response = sr.getResponse(request);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
            return;
        } catch (SAXException e) {
            e.printStackTrace();
            fail();
            return;
        }

        String text = null;
        try {
            text = response.getText();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
            return;
        }
        System.out.println("responded with: " + text);
    }
}
