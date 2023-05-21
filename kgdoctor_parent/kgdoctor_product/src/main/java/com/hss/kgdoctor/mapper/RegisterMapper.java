package com.hss.kgdoctor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.kgdoctor.common.domin.RegisterEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegisterMapper extends BaseMapper<RegisterEntity> {
}
