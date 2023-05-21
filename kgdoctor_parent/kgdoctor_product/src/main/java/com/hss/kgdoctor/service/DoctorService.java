package com.hss.kgdoctor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.kgdoctor.common.domin.DoctorEntity;
import com.hss.kgdoctor.domain.DoctorDTO;

public interface DoctorService extends IService<DoctorEntity> {
    DoctorDTO getDoctorDetail(Integer doctorId);
}
