package com.hss.kgdoctor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.kgdoctor.domin.KnowledgeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KnowledgeMapper extends BaseMapper<KnowledgeDTO> {
    List<KnowledgeDTO> getFuzzyContent(@Param("key") String key);
}
