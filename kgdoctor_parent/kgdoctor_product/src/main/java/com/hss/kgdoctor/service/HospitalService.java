package com.hss.kgdoctor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.kgdoctor.common.domin.HospitalEntity;

import java.util.List;

public interface HospitalService extends IService<HospitalEntity> {
    List<HospitalEntity> getPage(Integer size, Integer page);

    HospitalEntity getByHospitalId(Integer id);

//     HospitalEntity getByHospitalIdDB(Integer id);
}
