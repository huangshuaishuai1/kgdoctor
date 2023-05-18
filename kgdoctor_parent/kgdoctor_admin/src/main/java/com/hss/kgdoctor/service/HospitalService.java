package com.hss.kgdoctor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.kgdoctor.common.domin.HospitalEntity;

import java.util.List;

public interface HospitalService extends IService<HospitalEntity> {
    Boolean bind(Integer hospitalId, List<Integer> departmentIds);
}
