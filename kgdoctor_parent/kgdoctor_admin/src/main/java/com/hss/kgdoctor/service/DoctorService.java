package com.hss.kgdoctor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.kgdoctor.common.domin.DoctorEntity;

public interface DoctorService extends IService<DoctorEntity> {
    Integer getUserIdByEmail(String email);
}
