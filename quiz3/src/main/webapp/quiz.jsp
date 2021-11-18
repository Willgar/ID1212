<%--
  Created by IntelliJ IDEA.
  User: Willi
  Date: 2021-11-17
  Time: 21:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*,java.util.*" %>
<%@ page import="javax.servlet.*,java.text.*" %>
<jsp:useBean class="com.example.quiz3.model.Quiz" id="quizId" scope="session"></jsp:useBean>

<!DOCTYPE HTML>
<html>
<head>
    <title>Getting Started: Serving Web Content</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<%
    String url = "quiz";
    String next[] = quizId.getAnswers();
    if(!quizId.hasNext())
        url = "end";

%>
<form action=<%=url%> method="post">
    <label>Question:<%=quizId.getQuestion() %></label><br />
    <p><%=next[0] %></p><input type="checkbox" id="a" name="a"/>
    <p><%=next[1]%></p><input type="checkbox" id="b" name="b" >
    <p><%=next[2] %></p><input type="checkbox" id="c" name="c"/>
    <p><%=next[3] %></p><input type="checkbox" id="d" name="d"/>
    <br/><br/><input type="submit" value="Start" />
</form>
<p>Hello you got <%=quizId.getPoints() %></p>

</body>
</html>
