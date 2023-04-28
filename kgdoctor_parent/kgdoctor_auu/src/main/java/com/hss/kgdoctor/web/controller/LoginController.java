package com.hss.kgdoctor.web.controller;

import com.hss.kgdoctor.common.web.Result;
import com.hss.kgdoctor.domin.LoginUser;
import com.hss.kgdoctor.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@Slf4j
public class LoginController {

    @Autowired
    IUserService userService;

    @PostMapping
    public Result<String> login(@RequestBody LoginUser userLogin){

        //进行登录，并将这个token返回给前台
        String token = userService.login(userLogin.getEmail(),userLogin.getPassword());
        return Result.success(token);
    }

}
