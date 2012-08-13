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
<%@ page import="com.thenaglecode.sendalist.server.Constants" %>
<%
    UserAccount user = (UserAccount) session.getAttribute(ContextLoader.SESSION_USER_KEY);
%>
<html>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/jquery-ui.min.js"></script>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/googleapis/0.0.4/googleapis.min.js"></script>
<script type="text/javascript" src="//ajax.googleapis.com/jsapi"></script>
<script type="text/javascript" src="<%= Constants.SERVLET_URL_ACCOUNT_CHOOSER_JAVASCRIPT %>?id=navbar"/>
<body>
<div style="width: 320px; height: 290px; margin: 0 auto;" id="navbar">
</div>
</body>

</html>