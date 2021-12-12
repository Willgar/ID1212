package com.example.quiz3;

import com.example.quiz3.model.Quiz;

import java.io.*;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", urlPatterns = {"/quiz" , "/end"} , initParams = {
        @WebInitParam(name="name", value="Not provided"),
        @WebInitParam(name="password", value="Not provided"),
        @WebInitParam(name="email", value="Not provided"),
        @WebInitParam(name="a", value="Not provided"),
        @WebInitParam(name="b", value="Not provided"),
        @WebInitParam(name="c", value="Not provided"),
        @WebInitParam(name="d", value="Not provided"),
})
public class HelloServlet extends HttpServlet {
    private String message;
    Quiz quizId;

    public HelloServlet() throws ClassNotFoundException, SQLException {
    }

    public void init() {
        try {
            Quiz quizId = new Quiz();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        String test = request.getParameter("name");
        RequestDispatcher reqD = request.getRequestDispatcher("quiz.jsp");
        HttpSession httpSession = request.getSession(true);
        try {
            quizId = new Quiz(); //Reset
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        httpSession.setAttribute("quizId", quizId);
        reqD.forward(request, response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        String test = request.getParameter("name");
        RequestDispatcher reqD;
        if(request.getRequestURI().contains("end"))
            reqD = request.getRequestDispatcher("end.jsp");
        else
            reqD = request.getRequestDispatcher("quiz.jsp");
        HttpSession httpSession = request.getSession(true);
        if(request.getParameter("a")!= null) {
            quizId.guess("a");
        } else if(request.getParameter("b")!= null) {
            quizId.guess("b");
        } else if(request.getParameter("c")!= null) {
            quizId.guess("c");
        } else if(request.getParameter("d")!= null){
            quizId.guess("d");
        }
        httpSession.setAttribute("quizId", quizId);
        reqD.forward(request, response);
    }
    public void destroy() {
    }
}

