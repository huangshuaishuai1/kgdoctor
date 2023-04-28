package com.hss.kgdoctor.web.controller;

import com.hss.kgdoctor.common.web.Result;
import com.hss.kgdoctor.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sec")
public class SeckillController {

    @Autowired
    IStockService stockService;
    @PostMapping("/do/{id}")
    public Result doSeckill(@PathVariable("id") Long id, @RequestHeader("token") String token) {
        return stockService.doSeckill(id, token);
    }
}
