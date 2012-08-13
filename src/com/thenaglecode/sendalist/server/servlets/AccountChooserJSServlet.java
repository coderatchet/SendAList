package com.thenaglecode.sendalist.server.servlets;

import com.thenaglecode.sendalist.server.Constants;
import com.thenaglecode.sendalist.server.ContextLoader;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.UserAccount;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: jarednagle<br/>
 * Date: 12/08/12<br/>
 * Time: 7:38 PM<br/>
 * <br/>
 * This servlet takes an id of a html element as the value of the "id" paramater in the url string and attaches
 * it to the javascript
 */
public class AccountChooserJSServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        //check if user is logged in...
        resp.setContentType("text/javascript");
        String id = req.getParameter("id");
        if(id == null) id = "navbar";
        UserAccount user = (UserAccount) session.getAttribute(ContextLoader.SESSION_USER_KEY);
        StringBuilder js = new StringBuilder();
        js.append("google.load(\"identitytoolkit\",\"1\",{packages:[\"ac\"],language:\"en\"});");
        js.append("$(function(){")
                .append("window.google.identitytoolkit.setConfig({")
                .append("developerKey:\"").append(Constants.GOOGLE_API_KEY).append("\",")
                .append("callbackUrl:\"").append(Constants.URL_CALLBACK).append("\",")
                .append("realm:\"").append(Constants.URL_REALM).append("\",")
                .append("userStatusUrl:\"").append(Constants.URL_USER_STATUS).append("\",")
                .append("loginUrl:\"").append(Constants.URL_LOGIN).append("\",")
                .append("signupUrl:\"").append(Constants.URL_SIGN_UP).append("\",")
                .append("homeUrl:\"").append(Constants.URL_HOME).append("\",")
                .append("logoutUrl:\"").append(Constants.URL_LOGOUT).append("\",")
                .append("idps: [");
        boolean isFirst = true;
        for (int i = 0; i < Constants.IDPS.length; i++) {
            if (isFirst) {
                isFirst = false;
                js.append("\"").append(Constants.IDPS[i]);
            } else {
                js.append("\",\"").append(Constants.IDPS[i]);
            }
        }
        js.append("\"],")
                .append("tryFederatedFirst:").append(Constants.TRY_FEDERATED_FIRST).append(",")
                .append("useCachedUserStatus:").append(Constants.USER_CACHED_USER_STATUS).append(",")
                .append("useContextParam:").append(Constants.USE_CONTEXT_PARAM).append("");
        js.append("});");
        js.append("$(\"#").append(id).append("\").accountChooser()");
        if(user != null){
            js.append("var userData = {")
                    .append("email:\"").append(user.getEmail()).append("\",")
                    .append("displayName:\"").append(user.getDisplayName()).append("\",")
                    .append("photoUrl:\"").append(user.getPhotoUrl()).append("\"")
                    .append("};");
            js.append("window.google.identitytoolkit.updateSavedAccount(userData);");
            js.append("window.google.identitytoolkit.showSavedAccount(").append(user.getEmail()).append(")");
        }
        js.append("});");
        PrintWriter pw = resp.getWriter();
        pw.print(js.toString());
        pw.close();
    }
}
