<%--
  Created by IntelliJ IDEA.
  User: Jared Nagle
  Date: 8/08/12
  Time: 2:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.thenaglecode.sendalist.server.domain2Objectify.entities.UserAccount" %>
<%@ page import="com.thenaglecode.sendalist.server.ContextLoader" %>
<%
    UserAccount user = (UserAccount) session.getAttribute(ContextLoader.SESSION_USER_KEY);
%>
<html>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/jquery-ui.min.js"></script>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/googleapis/0.0.4/googleapis.min.js"></script>
<script type="text/javascript" src="//ajax.googleapis.com/jsapi"></script>
<script type="text/javascript">
    google.load("identitytoolkit", "1", {packages: ["ac"], language:"en"});
</script>
<script type="text/javascript">
    $(function() {
        window.google.identitytoolkit.setConfig({
            developerKey: "AIzaSyCk5yd7Qt3vQGAuXaUpeLdD2yKgx5enmis",
            companyName: "The Nagle Code",
            callbackUrl: "http://thenaglecode.broke-it.net:8080/callback",
            realm: "",
            userStatusUrl: "/status",
            loginUrl: "/login",
            signupUrl: "/signup.jsp",
            homeUrl: "/secured/SendAList.jsp",
            logoutUrl: "/logout",
            idps: ["Gmail", "GoogleApps", "Yahoo", "Hotmail"],
            tryFederatedFirst: true,
            useCachedUserStatus: false,
            useContextParam: true
        });
        $("#navbar").accountChooser();
        <% if (user != null) { %>
        var userData = {
            email: '<%= user.getEmail() %>',
            displayName: '<%= user.getDisplayName() %>',
            photoUrl: '<%= user.getPhotoUrl() %>'
        };
        window.google.identitytoolkit.updateSavedAccount(userData);
        window.google.identitytoolkit.showSavedAccount('<%= user.getEmail() %>');
        <% } %>
    });
</script>
<!-- Insert the navbar element somewhere in the HTML page -->
<body>
<div style="width: 320px; height: 290px; margin: 0 auto;">
    <a id="navbar"></a>
</div></body>

</html>