package com.jobhunter.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

    @GetMapping("/")
    public  String home(){
        return "Hello Khánh Đức";
    }
}
