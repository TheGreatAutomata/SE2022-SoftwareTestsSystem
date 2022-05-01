package com.controller;

import com.respond.Greeting;
import com.respond.ReportRespond;
import com.respond.UsrDelegationRespond;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsrDelegationController {
    @GetMapping("/user/delegation/submit")
    public UsrDelegationRespond usrDelegationRespond () {
        return new UsrDelegationRespond("submit successfully");
    }
}
