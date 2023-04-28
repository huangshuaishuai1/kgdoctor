package com.hss.kgdoctor.web.controller;

import com.hss.kgdoctor.common.constants.CommonConstants;
import com.hss.kgdoctor.common.util.UserHolder;
import com.hss.kgdoctor.common.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hss.kgdoctor.common.redis.CommonRedisKey.USER_TOKEN;


@RestController
@RequestMapping("/logout")
@Slf4j
public class LogoutController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @GetMapping
    public Result logout(@RequestHeader(CommonConstants.TOKEN_NAME) String token) {
        UserHolder.removeUser();
        // 将Redis中的token缓存删了
        String key = USER_TOKEN + token;
        stringRedisTemplate.delete(key);
        return Result.success();
    }
}
