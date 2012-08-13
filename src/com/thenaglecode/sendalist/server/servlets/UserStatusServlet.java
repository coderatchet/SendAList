package com.thenaglecode.sendalist.server.servlets;

import com.google.gson.JsonObject;
import com.thenaglecode.sendalist.server.domain2Objectify.SendAListDAO;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.UserAccount;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 3/06/12
 * Time: 1:51 AM
 * <p/>
 * <br/>accepts the email and returns a json object: { "registered": {boolean} }
 * <br/>
 * <br/>Request: POST /status
 * <br/>Parameters: email=name@domain.com
 */
public class UserStatusServlet extends HttpServlet {

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
        if (email == null) { //email regex
            answer.addProperty("registered", false);
        } else {
            answer.addProperty("registered", checkRegistrationStatus(email));
        }
        out.print(answer.toString());
        out.flush();
        out.close();
    }

    private boolean checkRegistrationStatus(final String email) {
        SendAListDAO dao = new SendAListDAO();
        UserAccount user = dao.findUser(email);
        return user != null;
    }
}
