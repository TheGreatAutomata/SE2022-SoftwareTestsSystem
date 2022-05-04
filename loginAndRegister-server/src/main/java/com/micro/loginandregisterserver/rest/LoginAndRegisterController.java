package com.micro.loginandregisterserver.rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.micro.loginandregisterserver.TempBodyForLoginResult.TempBodyForLoginResult;


@RestController
public class LoginAndRegisterController{

    @PostMapping("/login")
    public TempBodyForLoginResult example(@RequestBody TempBodyForLoginResult theRequest)
    {
        return theRequest;
    }
}
