package com.hss.kgdoctor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.kgdoctor.common.domin.HospitalEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HospitalMapper extends BaseMapper<HospitalEntity> {
    Integer bind(@Param("hospitalId") Integer hospitalId, @Param("departmentId") Integer departmentId);

    Integer selectIsExit(@Param("hospitalId") Integer hospitalId, @Param("departmentId") Integer departmentId);
}
