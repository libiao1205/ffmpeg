package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author libiao
 * @date 2023/12/13
 */
@Controller
public class HtmlController {

    @GetMapping("/")
    public String example() {
        return "dist/index";
    }

    @GetMapping("/home")
    public String html() {
        return "dist/index";
    }
}
