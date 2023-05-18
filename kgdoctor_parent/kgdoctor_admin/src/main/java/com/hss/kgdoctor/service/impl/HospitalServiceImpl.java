package com.hss.kgdoctor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.kgdoctor.common.domin.HospitalEntity;
import com.hss.kgdoctor.common.util.AppException;
import com.hss.kgdoctor.common.util.AppExceptionCodeMsg;
import com.hss.kgdoctor.mapper.HospitalMapper;
import com.hss.kgdoctor.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, HospitalEntity> implements HospitalService {
    @Autowired
    HospitalMapper hospitalMapper;
    @Override
    public Boolean bind(Integer hospitalId, List<Integer> departmentIds) {
        for (Integer departmentId : departmentIds) {
            Integer count = hospitalMapper.selectIsExit(hospitalId, departmentId);
            if (count == 0) {
                Integer bind = hospitalMapper.bind(hospitalId, departmentId);
                if (bind == 0) {
                    throw new AppException(AppExceptionCodeMsg.BIND_HOSPITAL_DEPARTMENT_FAIL);
                }
            }
        }
        return true;
    }
}
