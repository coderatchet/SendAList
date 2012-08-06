package com.thenaglecode.sendalist.server.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 6/08/12
 * Time: 2:07 PM
 *
 * This servlet handles the requests for information. it is strictly a "GET" service and no post data is processed.
 *
 * the syntax is similar to Google's apis. the scope with the user and then the information needed.
 *
 * possible combinations include:
 * www.sendalist.com/info/jared@sendalist.com/?c=lists //will also get all lists for jared@sendalist.com
 * www.sendalist.com/info/jared@sendalist.com/ //gets all information relevant to initiating a user's page. i.e. all list titles, ids. invitations and also any
 * www.sendalist.com/info/jared@sendalist.com/?c=lists&i=1234 //get the list with id of 1234
 * www.sendalist.com/info/jared@sendalist.com/?c=inv //get all invitations sent to jared@sendalist.com
 */
public class InfoServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
