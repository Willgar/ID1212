<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello Quiz</title>
</head>
<body>
<h3>Welcome to the Quiz!</h3>
<form action="quiz" method="get">
    <label for="name">Username:</label><br />
    <input type="text" id="name" name="name" /> <br />
    <label for="passw">Password:</label><br />
    <input type="text" id="passw" name="password" /> <br />
    <label for="email">Email:</label><br />
    <input type="text" id="email" name="email" /><br /><br />
    <input type="submit" value="Start" />
</form></body>
</html>