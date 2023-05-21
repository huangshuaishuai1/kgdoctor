package com.hss.kgdoctor.mapper;

import com.hss.kgdoctor.common.domin.RegisterEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RegisterMapper {
    RegisterEntity getById(@Param("id") Integer id);
}
