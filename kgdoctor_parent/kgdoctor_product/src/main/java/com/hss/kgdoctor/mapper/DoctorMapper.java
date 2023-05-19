package com.hss.kgdoctor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.kgdoctor.common.domin.DoctorEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DoctorMapper extends BaseMapper<DoctorEntity> {
}
