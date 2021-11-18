<%--
  Created by IntelliJ IDEA.
  User: Willi
  Date: 2021-11-17
  Time: 21:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean class="com.example.quiz3.model.Quiz" id="quizId" scope="session"></jsp:useBean>

<!DOCTYPE HTML>
<html>
<head>
    <title>Getting Started: Serving Web Content</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<p>The end, you got <%=quizId.getPoints() %></p>

</body>
</html>
