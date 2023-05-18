package com.hss.kgdoctor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.kgdoctor.common.domin.DoctorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DoctorMapper extends BaseMapper<DoctorEntity> {
    Integer getUserIdByEmail(@Param("email") String email);
}
