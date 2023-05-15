package com.hss.kgdoctor.controller;

import com.hss.kgdoctor.common.web.Result;
import com.hss.kgdoctor.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inquiry")
public class InquiryController {

    @Autowired
    InquiryService inquiryService;
    @GetMapping("/{doctorId}")
    public Result buyInquiry(@PathVariable("doctorId") Long doctorId, @RequestHeader("token") String token) {
        return inquiryService.buyInquiry(doctorId, token);
    }
}
