package com.hss.kgdoctor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hss.kgdoctor.common.domin.RegisterEntity;
import com.hss.kgdoctor.common.web.Resp;
import com.hss.kgdoctor.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
public class StockController {
    @Autowired
    RegisterService registerService;
    // 根据挂号id减库存
    @PostMapping("/regster/dec/{id}")
    public Resp decrStock(@PathVariable Integer registerId) {
        registerService.decrStock(registerId);
        return Resp.success("减库存成功");
    }

}
