package com.hss.kgdoctor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.kgdoctor.common.domin.DoctorEntity;
import com.hss.kgdoctor.common.domin.DoctorVO;
import com.hss.kgdoctor.mapper.DoctorInfoMapper;
import com.hss.kgdoctor.service.IDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorInfoMapper, DoctorEntity> implements IDoctorService {

    @Autowired
    DoctorInfoMapper doctorInfoMapper;

    @Override
    public DoctorEntity getDoctorInfoById(Long id) {
        DoctorEntity doctor = this.getById(id);
        return doctor;
    }

    @Override
    public DoctorVO getDoctorInfoWithSpecialtyById(Long id) {
        DoctorVO doctorWithSpecialty = doctorInfoMapper.getDoctorWithSpecialty(id);
        return doctorWithSpecialty;
    }


}
