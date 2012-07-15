package com.thenaglecode.sendalist.server.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 8/07/12
 * Time: 1:30 PM
 */
public class RedirectorServlet extends HttpServlet {
    private String redirect = null;

    public void init() throws ServletException {
        this.redirect = getInitParameter("redirect");
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        if (redirect == null)
            throw new ServletException("Cannot redirect to null.");
        else
            response.sendRedirect(redirect);
    }
}
