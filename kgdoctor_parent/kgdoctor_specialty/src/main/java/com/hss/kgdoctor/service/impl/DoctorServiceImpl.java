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
        if (doctorWithSpecialty == null) {
            // 可能存在还没有帮这个医生设置科室的情况，这里就返回其他属性就行
            DoctorEntity doctorEntity = this.getById(id);
            doctorWithSpecialty.setDoctorName(doctorEntity.getDoctorName());
            doctorWithSpecialty.setInquiryPrice(doctorEntity.getInquiryPrice());
            doctorWithSpecialty.setHospital(doctorEntity.getHospital());
            doctorWithSpecialty.setTitle(doctorEntity.getTitle());
            doctorWithSpecialty.setIntroduction(doctorEntity.getIntroduction());
        }
        return doctorWithSpecialty;
    }


}
