package com.thenaglecode.sendalist.server.servlets;

import com.thenaglecode.sendalist.server.domain2Objectify.SendAListDAO;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.UserAccount;
import com.thenaglecode.sendalist.shared.OriginatorOfPersistentChange;
import com.thenaglecode.sendalist.shared.dto.ErrorSet;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 6/08/12
 * Time: 2:07 PM
 * <p/>
 * This servlet handles the requests for information. it is strictly a "GET" service and no post data is processed.
 * <p/>
 * the syntax is similar to Google's apis. the scope with the user and then the information needed.
 * <p/>
 * possible combinations include:
 * www.sendalist.com/info/jared@sendalist.com/?c=lists //will also get all lists for jared@sendalist.com
 * www.sendalist.com/info/jared@sendalist.com/ //gets all information relevant to initiating a user's page. i.e. all list titles, ids. invitations and also any
 * www.sendalist.com/info/jared@sendalist.com/?c=lists&i=1234 //get the list with id of 1234
 * www.sendalist.com/info/jared@sendalist.com/?c=inv //get all invitations sent to jared@sendalist.com
 */
public class InfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        OriginatorOfPersistentChange context = OriginatorOfPersistentChange.getContext(req);

        if (pathInfo == null) return;
        String decoded = URLDecoder.decode(pathInfo, "UTF-8");
        String terms[] = null;
        SendAListDAO dao;
        if (decoded.startsWith("/")) {
            terms = decoded.substring(1).split("/");
        } else {
            terms = decoded.split("/");
        }
        if (terms.length > 0) {
            String email = terms[0];
            dao = new SendAListDAO();
            UserAccount user = dao.findUser(email);
            if (user == null) {
                returnError(resp, "could not find user with email" + email);
                return;
            } else {
                try {
                    JSONObject data = user.getHomePageStartUpData();
                    returnJson(resp, data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void returnError(HttpServletResponse res, String message) throws IOException {
        ErrorSet set = new ErrorSet(400, "Bad Request", message);
        PrintWriter writer = res.getWriter();
        res.setContentType("application/json");
        String whole = set.toString();
        res.setContentLength(whole.length());
        res.setCharacterEncoding("UTF-8");
        writer.write(whole);
        writer.flush();
        writer.close();
    }

    private void returnJson(HttpServletResponse res, JSONObject json) throws IOException {
        PrintWriter writer = res.getWriter();
        res.setContentType("application/json");
        String whole = json.toString();
        res.setContentLength(whole.length());
        res.setCharacterEncoding("UTF-8");
        writer.write(whole);
        writer.flush();
        writer.close();
    }
}
