package com.thenaglecode.sendalist.server.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 27/07/12
 * Time: 4:53 PM
 */
public class LogoutServlet extends HttpServlet{
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        if(session != null) session.invalidate();
        res.sendRedirect("login.jsp");
    }
}
