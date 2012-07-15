package com.thenaglecode.sendalist.server.servlets;

import com.thenaglecode.sendalist.server.Globals;
import com.thenaglecode.sendalist.server.domain2Objectify.SendAListDAO;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.UserAccount;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 8/07/12
 * Time: 9:59 PM
 */
public class TestServlet extends HttpServlet {
    String BAD_REQUEST_PAGE = "<!DOCTYPE html><html lang=\"en\"><head><title>Error</title></head><body><h1>Error</h1><h2>Error Code: 400</h2><p>Sorry, there appears to have been an error with that request!</p></body></html>";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        service(req, res);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        service(req, res);
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) {
        Map<String, String> queryParams = new HashMap<String, String>();

        if (req.getQueryString() != null) {
            String[] terms = req.getQueryString().split("&");
            for (String term : terms) {
                int point = term.indexOf("=");
                if (point == -1) break; // break if name/value pair not formed correctly
                String name = term.substring(0, point).trim();
                String value = term.substring(point + 1).trim();
                if (value.isEmpty())
                    break;
                else {
                    System.out.println(name + "=" + value);
                    queryParams.put(name, value);
                }
            }
        }

        String command = queryParams.get("c");
        if (command != null) {
            processCommand(command, queryParams, res);
        } else {
            try {
                PrintWriter writer = res.getWriter();
                writer.append("<!DOCTYPE html>");
                writer.append("<html lang=\"en\">");
                writer.append("<head><title>test</title></head><body><p><h1>test text</h1></p></body></html>");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processCommand(@NotNull String command, Map<String, String> queryParams, HttpServletResponse res) {
        if ("gen".equals(command)) {
            String num = queryParams.get("num");
            int value;
            if (num == null) {
                returnErrorPage(400, res);
                return;
            }
            try {
                value = Integer.valueOf(num);
            } catch (NumberFormatException e) {
                returnErrorPage(400, res);
                return;
            }
            try {
                Globals.createAndPersistFakeUsers(value);
            } catch (Exception e) {
                e.printStackTrace();
            }

            printHtmlInBody("done", "<h2>created " + value + " fake users</h2>", res);
        } else if ("send".equals(command)) {
            //todo once send a list is implemented, this will 'send' a given list to a specified friend.
        } else if ("printusers".equals(command)) {
            SendAListDAO dao = new SendAListDAO();
            List<UserAccount> users = dao.getAllUsers();
            StringBuilder sb = new StringBuilder();
            for (UserAccount user : users) {
                sb.append(user.getTaskListsAsHtmlString());
            }
            printHtmlInBody("all users", sb.toString(), res);
        }
    }

    private void printHtmlInBody(String title, String html, HttpServletResponse res) {
        try {
            PrintWriter writer = res.getWriter();
            writer.append("<!DOCTYPE html><html lang=\"en\"><head><title>").append(title)
                    .append("</title></head><body>")
                    .append(html)
                    .append("</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void returnErrorPage(int errorCode, HttpServletResponse res) {
        switch (errorCode) {
            case 400: {
                badRequest(res);
                break;
            }
        }
    }

    private void badRequest(HttpServletResponse res) {
        try {
            PrintWriter writer = res.getWriter();
            writer.append(BAD_REQUEST_PAGE);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}