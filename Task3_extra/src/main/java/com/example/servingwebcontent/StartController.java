package com.example.servingwebcontent;

import model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.Random;


@org.springframework.stereotype.Controller
public class StartController {
    Quiz quiz;
    @GetMapping("/")
    public String greeting1(Model model) throws SQLException, ClassNotFoundException {
        quiz = new Quiz();
        return "greeting";
    }

    @GetMapping("/quiz")
    public String quiz(@RequestParam(required = false) String a,
                       @RequestParam(required = false) String b,
                       @RequestParam(required = false) String c,
                       @RequestParam(required = false) String d,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) String password,
                       @RequestParam(required = false) String email,
                       Model model){
        if(a!= null) {
            System.out.println(a);
            model.addAttribute("answer", "a");
            model.addAttribute("correct", quiz.guess("a"));
        } else if(b!= null) {
            System.out.println(b);
            model.addAttribute("answer", "b");
            model.addAttribute("correct", quiz.guess("b"));
        } else if(c!= null) {
            System.out.println(c);
            model.addAttribute("answer", "c");
            model.addAttribute("correct", quiz.guess("c"));
        } else if(d!= null){
            System.out.println(d);
            model.addAttribute("answer", "d");
            model.addAttribute("correct", quiz.guess("d"));
        }
        String next[] = quiz.getAnswers();
        model.addAttribute("question", quiz.getQuestion());
        model.addAttribute("points", quiz.getPoints());
        model.addAttribute("name", name);
        model.addAttribute("password", password);
        model.addAttribute("email", email);
        model.addAttribute("a", next[0]);
        model.addAttribute("b", next[1]);
        model.addAttribute("c", next[2]);
        model.addAttribute("d", next[3]);
        if(quiz.hasNext()){
            model.addAttribute("url", "quiz");
        } else {
            model.addAttribute("url", "end");
        }
        return "quiz";
    }
    @GetMapping("/end")
    public String end(@RequestParam(required = false) String a,
                      @RequestParam(required = false) String b,
                      @RequestParam(required = false) String c,
                      @RequestParam(required = false) String d,
                      Model model){
        if(a!= null) {
            System.out.println(a);
            model.addAttribute("answer", "a");
            model.addAttribute("correct", quiz.guess("a"));
        } else if(b!= null) {
            System.out.println(b);
            model.addAttribute("answer", "b");
            model.addAttribute("correct", quiz.guess("b"));
        } else if(c!= null) {
            System.out.println(c);
            model.addAttribute("answer", "c");
            model.addAttribute("correct", quiz.guess("c"));
        } else if(d!= null){
            System.out.println(d);
            model.addAttribute("answer", "d");
            model.addAttribute("correct", quiz.guess("d"));
        }
        model.addAttribute("points", quiz.getPoints());
        return "end";
    }

}
