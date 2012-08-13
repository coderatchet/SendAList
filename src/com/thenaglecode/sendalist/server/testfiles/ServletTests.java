package com.thenaglecode.sendalist.server.testfiles;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.JsonObject;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.InvocationContext;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import com.thenaglecode.sendalist.server.Constants;
import com.thenaglecode.sendalist.server.ContextLoader;
import com.thenaglecode.sendalist.server.domain2Objectify.SendAListDAO;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.Task;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.TaskList;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.UserAccount;
import com.thenaglecode.sendalist.server.domain2Objectify.interfaces.Processable;
import com.thenaglecode.sendalist.server.domain2Objectify.util.InvitationManager;
import com.thenaglecode.sendalist.server.servlets.AccountChooserJSServlet;
import com.thenaglecode.sendalist.server.servlets.InfoServlet;
import com.thenaglecode.sendalist.server.servlets.TransactionServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
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
    private long listId = 0;


    @Before
    public void setUp() {
        helper.setUp();
        runner = new ServletRunner();
        //we use the strings with the forward slash removed to keep to conventions of the servletUnit methods
        runner.registerServlet(Constants.SERVLET_URL_TRANSACTION_API.substring(1), TransactionServlet.class.getName());
        runner.registerServlet(Constants.SERVLET_URL_INFO_API.substring(1), InfoServlet.class.getName());
        runner.registerServlet(Constants.SERVLET_URL_ACCOUNT_CHOOSER_JAVASCRIPT.substring(1),
                AccountChooserJSServlet.class.getName());
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
        try {
            createOwner();
            testCreateMultipleLists();
            testCreateMultipleTasks();
            testRenameTask();
            testDeleteTask();
            testRenameList();
            testInvitationSending();
            testMultipleViewInvitations();
            testUpgradeToEdit();
            testInvitationCopy();
            testInfoServlet();
            testAccountChooserJavaScriptServlet();
            testDeleteList();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testInfoServlet() throws IOException, SAXException {
        System.out.println("/****************************/");
        String urlString;
        if(Constants.SERVLET_URL_INFO_API.endsWith("*")) urlString = Constants.SERVLET_URL_INFO_API.substring(0, Constants.SERVLET_URL_INFO_API.length()-2);
        else urlString = Constants.SERVLET_URL_INFO_API;
        System.out.println(sc.getResponse("http://test.com" + urlString + "/jared@sendalist.com").getText());
    }

    /**  */
    private void createOwner() throws JSONException {
        JSONObject responseJson;
        String transaction;
        final String expectedEmail = "jared@sendalist.com";
        final String expectedFName = "Jared";
        final String expectedLName = "Nagle";
        final String expectedDisplayName = "hullabaloo";
        final String expectedPass = "1234";

        transactionWithFile(new JSONObject(getTextFromFile("sign_up.txt")));

        SendAListDAO dao = new SendAListDAO();
        UserAccount userAccount = dao.findUser(expectedEmail);
        assertNotNull(userAccount);
        assertEquals("expected the email to be \"" + expectedEmail + "\"", expectedEmail, userAccount.getEmail());
        assertEquals("expected first name to be \"" + expectedFName + "\"", expectedFName, userAccount.getFirstName());
        assertEquals("expected last name to be \"" + expectedLName + "\"", expectedLName, userAccount.getLastName());
        assertEquals("expected display name to be \"" + expectedDisplayName + "\"", expectedDisplayName, userAccount.getDisplayName());
        assertTrue("expected password to be \"" + expectedPass + "\"", userAccount.checkPassword(expectedPass));
    }

    private void testCreateMultipleLists() throws JSONException {
        JSONObject responseJson = null;
        String transaction;
        final String expectedSummary = "first list";
        final String expectedOwner = "jared@sendalist.com";

        responseJson = transactionWithFile(new JSONObject(getTextFromFile("multiple_lists.txt")));

        try {
            listId = responseJson.getJSONArray("responses").getJSONObject(0).getLong(Processable.FIELD_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assertTrue("the id should have been returned", listId > 0);
        SendAListDAO dao = new SendAListDAO();
        TaskList taskList = dao.findTaskList(listId);
        assertNotNull(taskList);
        assertEquals("expected summary to be \"" + expectedSummary + "\"", expectedSummary, taskList.getSummary());
        assertEquals("expectedOwner to be \"" + expectedOwner + "\"", expectedOwner, taskList.getOwner().getName());
        assertEquals("expected list to have 0 items", 0, taskList.getTasks().size());
    }

    private void testCreateMultipleTasks() throws JSONException {
        JSONObject requestJson = null, responseJson = null;
        List<Task> expectedTasks = Arrays.asList(
                new Task("done").setDone(true),
                new Task("not done"),
                new Task("done also").setDone(true));

        requestJson = new JSONObject(getTextFromFile("multiple_tasks.txt"));
        requestJson.getJSONArray("txs").getJSONObject(0).put("i", listId);
        transactionWithFile(requestJson);

        SendAListDAO dao = new SendAListDAO();
        TaskList taskList = dao.findTaskList(listId);
        assertNotNull(taskList);
        assertEquals("expected taskList to have " + expectedTasks.size() + " tasks", expectedTasks.size(), taskList.getTasks().size());
        for (Task task : expectedTasks) {
            boolean contains = false;
            for (Task actualTask : taskList.getTasks()) {
                if (task.equalsIgnoreTimestamp(actualTask)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) fail("expected the list to contain task: " + task.toJson());
        }
    }

    private void testRenameTask() throws JSONException {
        JSONObject requestJson = null, responseJson = null;
        List<Task> expectedTasks = Arrays.asList(
                new Task("done").setDone(true),
                new Task("hmmmm"),
                new Task("done also").setDone(true));

        requestJson = new JSONObject(getTextFromFile("rename_task.txt"));
        requestJson.getJSONArray("txs").getJSONObject(0).put("i", listId);
        transactionWithFile(requestJson);

        SendAListDAO dao = new SendAListDAO();
        TaskList taskList = dao.findTaskList(listId);
        assertNotNull(taskList);
        assertEquals("expected taskList to have " + expectedTasks.size() + " tasks", expectedTasks.size(), taskList.getTasks().size());
        for (Task task : expectedTasks) {
            boolean contains = false;
            for (Task actualTask : taskList.getTasks()) {
                if (task.equalsIgnoreTimestamp(actualTask)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) fail("expected the list to contain task: " + task.toJson());
        }
    }

    private void testDeleteTask() throws JSONException {
        JSONObject requestJson = null, responseJson = null;
        List<Task> expectedTasks = Arrays.asList(
                new Task("done").setDone(true),
                new Task("done also").setDone(true));

        requestJson = new JSONObject(getTextFromFile("delete_task.txt"));
        requestJson.getJSONArray("txs").getJSONObject(0).put("i", listId);
        transactionWithFile(requestJson);

        SendAListDAO dao = new SendAListDAO();
        TaskList taskList = dao.findTaskList(listId);
        assertNotNull(taskList);
        assertEquals("expected taskList to have " + expectedTasks.size() + " tasks", expectedTasks.size(), taskList.getTasks().size());
        for (Task task : expectedTasks) {
            boolean contains = false;
            for (Task actualTask : taskList.getTasks()) {
                if (task.equalsIgnoreTimestamp(actualTask)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) fail("expected the list to contain task: " + task.toJson());
        }
    }

    private void testRenameList() throws JSONException {
        JSONObject requestJson = null, responseJson = null;
        final String expectedSummary = "new summary here";

        requestJson = new JSONObject(getTextFromFile("rename_list.txt"));
        requestJson.getJSONArray("txs").getJSONObject(0).put("i", listId);
        transactionWithFile(requestJson);

        SendAListDAO dao = new SendAListDAO();
        TaskList taskList = dao.findTaskList(listId);
        assertNotNull(taskList);
        assertEquals("expected tasklist to have new summary of \"" + expectedSummary + "\"", expectedSummary, taskList.getSummary());
    }

    private void testDeleteList() throws JSONException {
        JSONObject requestJson = null, responseJson = null;
        requestJson = new JSONObject(getTextFromFile("delete_list.txt"));
        requestJson.getJSONArray("txs").getJSONObject(0).put("i", listId);
        transactionWithFile(requestJson);
        SendAListDAO dao = new SendAListDAO();
        TaskList taskList = dao.findTaskList(listId);
        assertNull(taskList);
    }

    private void testInvitationSending() throws JSONException {
        JSONObject requestJson = null, responseJson = null;
        requestJson = new JSONObject(getTextFromFile("send_a_list.txt"));
        transactionWithFile(requestJson);
        InvitationManager.getInstance().printState();
    }

    private void testMultipleViewInvitations() throws JSONException {
        JSONObject requestJson = null, responseJson = null;
        requestJson = new JSONObject(getTextFromFile("inv_view.txt"));
        requestJson.getJSONArray("txs").getJSONObject(1).put("id", listId);
        responseJson = transactionWithFile(requestJson);
        JSONArray responses = responseJson.getJSONArray("responses");
        int length = responses.length();
        for(int i=0; i<length; i++){
            JSONObject obj = (JSONObject) responses.get(i);
            assertEquals(obj.getInt("code"), 200);
        }

        InvitationManager.getInstance().printState();
    }

    private void testUpgradeToEdit() throws JSONException {
        JSONObject requestJson = null, responseJson = null;
        requestJson = new JSONObject(getTextFromFile("inv_edit_upgrade.txt"));
        responseJson = transactionWithFile(requestJson);
        JSONArray responses = responseJson.getJSONArray("responses");
        int length = responses.length();
        for(int i=0; i<length; i++){
            JSONObject obj = (JSONObject) responses.get(i);
            assertEquals(obj.getInt("code"), 200);
        }

        InvitationManager.getInstance().printState();
    }

    private void testInvitationCopy() throws JSONException {
        JSONObject requestJson = null, responseJson = null;
        requestJson = new JSONObject(getTextFromFile("inv_copy.txt"));
        responseJson = transactionWithFile(requestJson);
        JSONArray responses = responseJson.getJSONArray("responses");
        int length = responses.length();
        for(int i=0; i<length; i++){
            JSONObject obj = (JSONObject) responses.get(i);
            assertEquals(obj.getInt("code"), 200);
        }

        InvitationManager.getInstance().printState();
    }

    public void testAccountChooserJavaScriptServlet() throws IOException, SAXException {
        WebRequest request = new GetMethodWebRequest("http://test.com"
                + Constants.SERVLET_URL_ACCOUNT_CHOOSER_JAVASCRIPT);
        System.out.println("Servlet response without user: \n" + sc.getResponse(request).getText());

        //and with a logged in user...
        UserAccount loggedInUser = new UserAccount()
                .setEmail("someemail@someplace.com")
                .setDisplayName("lalala")
                .setPhotoUrl("http://url/for/photo");
        InvocationContext ic = sc.newInvocation(request);
        HttpSession session = ic.getRequest().getSession(true);
        session.setAttribute(ContextLoader.SESSION_USER_KEY, loggedInUser);
        System.out.println("\n\nServlet response with user: \n" + ic.getServletResponse().getText());
    }

    private JSONObject transactionWithFile(JSONObject requestJson) {

        try {
            JSONObject responseJson;
            String transaction = requestJson.toString();
            System.out.println("\nattempting transaction: ");
            System.out.println(requestJson.toString(2));
            responseJson = new JSONObject(doPostWithThisData(transaction));
            System.out.println("\nresponse from servlet: ");
            System.out.println(responseJson.toString(2));
            return responseJson;
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
            return new JSONObject();
        }
    }

    /**
     * @return the response text
     */
    private String doPostWithThisData(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
            String urlString = "http://test.com" + Constants.SERVLET_URL_TRANSACTION_API;
            WebRequest request = new PostMethodWebRequest(urlString, inputStream, "application/json; charset=UTF-8");
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
        String filePath = "/src/com/thenaglecode/sendalist/server/testfiles/";
        try {
            File file = new File(".");
            System.out.println("transaction object obtained from...");
            System.out.println("\t" + file.getCanonicalPath() + filePath + fileName);
            FileInputStream is = new FileInputStream(file.getCanonicalPath() + filePath + fileName);
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
