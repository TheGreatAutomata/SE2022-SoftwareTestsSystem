package com.controller;

import com.respond.ExampleRespond;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @GetMapping("/example")
    public ExampleRespond example(@RequestParam(value = "name", defaultValue = "default") String name, @RequestParam(value = "password", defaultValue = "default") String password) {
        return new ExampleRespond(name, password, "01 Level");
    }
}
