package com.hss.kgdoctor.controller;

import com.hss.kgdoctor.common.web.Resp;
import com.hss.kgdoctor.common.web.Result;
import com.hss.kgdoctor.service.RegisterService;
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
    RegisterService registerService;


    // 根据库存id上传
    @GetMapping("/{id}")
    @Transactional
    public Resp uploadById(@PathVariable("id") Integer id) {
        return registerService.uploadById(id);
    }

    @GetMapping("/time")
    @Transactional
    public Resp uploadByTime() {
        registerService.uploadByTime();
        return Resp.success("上传成功");
    }
}
