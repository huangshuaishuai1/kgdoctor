package com.hss.kgdoctor.controller;

import com.hss.kgdoctor.common.web.Resp;
import com.hss.kgdoctor.common.web.Result;
import com.hss.kgdoctor.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sec")
public class SeckillController {

    @Autowired
    SeckillService seckillService;
    @PostMapping("/do/{id}")
    public Resp doSeckill(@PathVariable("id") Integer id, @RequestHeader("token") String token) {
        return seckillService.doSeckill(id, token);
    }
}
