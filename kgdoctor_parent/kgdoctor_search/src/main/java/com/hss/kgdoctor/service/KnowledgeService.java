package com.hss.kgdoctor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.kgdoctor.domin.KnowledgeDTO;

import java.util.List;

public interface KnowledgeService extends IService<KnowledgeDTO> {
    List<KnowledgeDTO> getByFuzzy(String key);
}
