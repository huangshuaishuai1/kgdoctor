package com.hss.kgdoctor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.kgdoctor.common.domin.DoctorEntity;
import com.hss.kgdoctor.common.domin.DoctorVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DoctorInfoMapper extends BaseMapper<DoctorEntity> {

    public DoctorVO getDoctorWithSpecialty(Long doctorId);
}
