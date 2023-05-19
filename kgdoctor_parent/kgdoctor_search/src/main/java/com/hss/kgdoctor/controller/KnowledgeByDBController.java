package com.hss.kgdoctor.controller;

import com.hss.kgdoctor.common.web.Resp;
import com.hss.kgdoctor.domin.KnowledgeDTO;
import com.hss.kgdoctor.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/knowledge")
public class KnowledgeByDBController {
    @Autowired
    KnowledgeService knowledgeService;

    @GetMapping("/db")
    public Resp<List<KnowledgeDTO>> getByFuzzy(@RequestParam("key") String key) {
        List<KnowledgeDTO> list = knowledgeService.getByFuzzy(key);
        if (list.isEmpty()) {
            return Resp.success("搜索到零条数据，换个关键字试试呢！",list);
        }
        return Resp.success(list);
    }
}
