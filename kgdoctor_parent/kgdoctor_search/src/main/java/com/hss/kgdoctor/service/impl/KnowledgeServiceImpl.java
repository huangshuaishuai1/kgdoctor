package com.hss.kgdoctor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.kgdoctor.domin.KnowledgeDTO;
import com.hss.kgdoctor.mapper.KnowledgeMapper;
import com.hss.kgdoctor.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeServiceImpl extends ServiceImpl<KnowledgeMapper, KnowledgeDTO> implements KnowledgeService {
    @Autowired
    KnowledgeMapper knowledgeMapper;
    @Override
    public List<KnowledgeDTO> getByFuzzy(String key) {
        return knowledgeMapper.getFuzzyContent("%"+key+"%");
    }
}
