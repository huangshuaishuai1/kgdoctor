package com.hss.kgdoctor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.kgdoctor.common.domin.DoctorEntity;
import com.hss.kgdoctor.common.domin.DoctorVO;

public interface IDoctorService extends IService<DoctorEntity> {
    DoctorEntity getDoctorInfoById(Long id);

    DoctorVO getDoctorInfoWithSpecialtyById(Long id);
}
