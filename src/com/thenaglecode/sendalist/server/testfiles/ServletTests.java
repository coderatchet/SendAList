package com.thenaglecode.sendalist.server.testfiles;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.JsonObject;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import com.thenaglecode.sendalist.server.domain2Objectify.SendAListDAO;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.UserAccount;
import com.thenaglecode.sendalist.server.servlets.TransactionServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.MalformedURLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 21/07/12
 * Time: 10:18 PM
 */
public class ServletTests {

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private static ServletRunner runner;
    private static ServletUnitClient sc;
    private JsonObject info;


    @Before
    public void setUp() {
        helper.setUp();
        runner = new ServletRunner();
        runner.registerServlet("api", TransactionServlet.class.getName());
        sc = runner.newClient();
        info = new JsonObject();
    }

    @After
    public void tearDown() {
        runner = null;
        sc = null;
        info = null;
        helper.tearDown();
    }

    private void init() {
    }

    @Test
    public void testAll() {
        init();
        createOwner();
        testCreateMultipleLists();
        testCreateMultipleTasks();
        testRenameTask();
        testDeleteTask();
        testRenameList();
        testDeleteList();
    }

    /**  */
    private void createOwner() {
        JSONObject requestJson, responseJson;
        String transaction;
        final String expectedEmail = "jared@sendalist.com";
        final String expectedFName = "Jared";
        final String expectedLName = "Nagle";
        final String expectedDisplayName = "hullabaloo";
        final String expectedPass = "1234";

        try {
            requestJson = new JSONObject(getTextFromFile("sign_up.txt"));
            transaction = requestJson.toString();
            System.out.println("\nattempting transaction: ");
            System.out.println(requestJson.toString(2));
            responseJson = new JSONObject(doPostWithThisData(transaction));
            System.out.println("response: " + responseJson.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
            return;
        }

        SendAListDAO dao = new SendAListDAO();
        UserAccount userAccount = dao.findUser(expectedEmail);
        assertNotNull(userAccount);
        assertEquals("expected the email to be \"" + expectedEmail + "\"", expectedEmail, userAccount.getEmail());
        assertEquals("expected first name to be \"" + expectedFName + "\"", expectedFName, userAccount.getFirstName());
        assertEquals("expected last name to be \"" + expectedLName + "\"", expectedLName, userAccount.getLastName());
        assertEquals("expected display name to be \"" + expectedDisplayName + "\"", expectedDisplayName, userAccount.getDisplayName());
        assertTrue("expected password to be \"" + expectedPass + "\"", userAccount.checkPassword(expectedPass));
    }

    private void testCreateMultipleLists() {
        JSONObject requestJson, responseJson;
        String transaction;
        final String expectedSummary = "first list";
        final String expectedOwner = "jared@sendalist.com";

        try {
            requestJson = new JSONObject(getTextFromFile("multiple_lists.txt"));
            transaction = requestJson.toString();
            System.out.println("\nattempting transaction: ");
            System.out.println(requestJson.toString(2));
            responseJson = new JSONObject(doPostWithThisData(transaction));
            System.out.println("\nresponse: ");
            System.out.println(responseJson.toString(2));
            testAllResponseSuccess(responseJson);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
            return;
        }

        SendAListDAO dao = new SendAListDAO();
    }

    private void testCreateMultipleTasks() {
    }

    private void testRenameTask() {

    }

    private void testDeleteTask() {

    }

    private void testRenameList() {

    }

    private void testDeleteList() {

    }

    private void testAllResponseSuccess(JSONObject responseJson) {
        if (!responseJson.has("errors")) fail("the response did not contain a list of errorSets!");
        try {
            JSONArray array = responseJson.getJSONArray("errors");
            for (int i = 0; i < array.length(); i++) {
                JSONObject error = array.getJSONObject(i);
                int code = error.getInt("code");
                assertEquals("error code should be 200 for success", 200, code);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            fail("json parsing exception");
        }
    }

    /**
     * @return the response text
     */
    private String doPostWithThisData(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
            WebRequest request = new PostMethodWebRequest("http://test.com/api", inputStream, "application/json; charset=UTF-8");
            request.setParameter("Content-Type", "application/json");
            WebResponse response = sc.getResponse(request);
            return response.getText();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getTextFromFile(String fileName) {
        try {
            File file = new File(".");
            System.out.println(file.getCanonicalPath() + "\\src\\com\\thenaglecode\\sendalist\\server\\testfiles\\" + fileName);
            FileInputStream is = new FileInputStream(file.getCanonicalPath() + "\\src\\com\\thenaglecode\\sendalist\\server\\testfiles\\" + fileName);
            return new Scanner(is).useDelimiter("\\A").next();
        } catch (NoSuchElementException e) {
            return "";
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }
}
