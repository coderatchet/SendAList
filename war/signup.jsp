<%--
  Created by IntelliJ IDEA.
  User: Jared Nagle
  Date: 27/07/12
  Time: 4:52 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Signup to SendAList</title>
<style type="text/css">
    .centerTable {
        margin-top: 10em;
        margin-left: 10em;
        margin-right: 10em;
        min-width: 50em;
        border-color: #4682b4;
        border-radius: 0.5em;
        border-width: thick;
        border-style: solid;
        padding: 2em;
    }
    .field {
        text-align: right;
        min-width: 10em;
    }
    .entries {
        text-align: left;
        width: 15em;
    }
</style>
</head>
<body>
Hello! The time is now <%= new java.util.Date() %>
<table class="centerTable">
    <tr>
        <th colspan=2 style="text-align: center;" >
            <h2>Sign Up</h2>
        </th>
    </tr>
    <tr>
        <td class="field">
            <label for="input_email">Email:</label>
        </td>
        <td>
            <%
                String email = request.getParameter("email");
                if(email == null) email = "";
            %>
            <input class="entries" type="text" id="input_email" value="<%=email%>">
        </td>
    </tr>
</table>

</body>
</html>