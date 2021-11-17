package com.example.servingwebcontent;

import model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;


@org.springframework.stereotype.Controller
public class StartController {
    String array1[] = {"a", "b", "c", "d"};
    Quiz quiz = new Quiz(array1, "b");
    @GetMapping("/")
    public String greeting1() {
        return "greeting";
    }

    @GetMapping("/start")
    public String start(@RequestParam String name,
                        @RequestParam String password,
                        @RequestParam String email, Model model){
        model.addAttribute("name", name);
        model.addAttribute("password", password);
        model.addAttribute("email", email);
        return "start";
    }
    @GetMapping("/quiz")
    public String quiz(@RequestParam(required = false) String a,
                       @RequestParam(required = false) String b,
                       @RequestParam(required = false) String c,
                       @RequestParam(required = false) String d,Model model){
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
        model.addAttribute("a", "Text1");
        model.addAttribute("b", "Text2");
        model.addAttribute("c", "Text3");
        model.addAttribute("d", "Text4");
        quiz.setCorrect("d");
        return "quiz";
    }

}
