package com.controller;

import com.respond.ReportRespond;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {
    @GetMapping("/report/check/{id}")
    public ReportRespond reportRespond (@PathVariable("id") String id) {
        return new ReportRespond(id);
    }
}
