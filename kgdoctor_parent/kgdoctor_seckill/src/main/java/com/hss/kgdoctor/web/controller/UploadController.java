package com.hss.kgdoctor.web.controller;

import com.hss.kgdoctor.common.web.CodeMsg;
import com.hss.kgdoctor.common.web.Result;
import com.hss.kgdoctor.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    IStockService stockService;
    // 根据库存id上传
    @GetMapping("/{id}")
    @Transactional
    public Result uploadById(@PathVariable("id") Long id) {
        String msg = stockService.uploadById(id);
        if ("失败".equals(msg.substring(0,2))) {
            CodeMsg codeMsg = new CodeMsg(50000, msg.substring(2));
            return Result.error(codeMsg);
        }else {
            return Result.success("上传成功",null);
        }
    }

    @GetMapping("/time")
    @Transactional
    public Result uploadByTime() {
        stockService.uploadByTime();
        return Result.success();
    }
}
