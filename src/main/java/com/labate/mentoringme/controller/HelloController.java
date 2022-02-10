package com.labate.mentoringme.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("")
    public String sayHello() {
        return "[LABATE] Mentoring Me ###";
    }

}
