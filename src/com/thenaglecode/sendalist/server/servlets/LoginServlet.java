package com.thenaglecode.sendalist.server.servlets;

import com.google.gson.JsonObject;
import com.thenaglecode.sendalist.server.domain2Objectify.SendAListDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 3/06/12
 * Time: 9:34 PM
 */
public class LoginServlet extends HttpServlet {

    private static final String LOGIN_ERROR = "passwordError";
    private static final String LOGIN_OK = "OK";

    private static Map<String, String> emailsPasswordMap = new HashMap<String, String>();

    static {
        emailsPasswordMap.put("jarednagle@sendalist.com", "silly");
        emailsPasswordMap.put("aprilnagle@sendalist.com", "silly");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service(req, resp);
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        JsonObject answer = new JsonObject();
        resp.setContentType("application/json");

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        answer.addProperty("registered", login(email, password)); //handles null params
        out.append(answer.toString());
        out.flush();
        out.close();
    }

    /**
     * logs in a user, creating a session object and returns the status message of the loginUrl post. <br/>
     * <a href="https://developers.google.com/identity-toolkit/v1/acguide">
     * click here for specification regarding this servlet's response messages
     * </a:href>
     *
     * @param email    email of the user attempting to login
     * @param password the password of the user attempting to login
     * @return either "OK" or "passwordError" if there was an issue
     */
    private String login(String email, String password) {
        boolean success = new SendAListDAO().checkPassword(email, password);
        if(success){
            //log the user in

        }
        return success ? LOGIN_OK : LOGIN_ERROR;
    }
}
