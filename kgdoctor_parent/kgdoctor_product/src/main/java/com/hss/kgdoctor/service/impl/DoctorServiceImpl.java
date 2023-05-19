package com.hss.kgdoctor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.kgdoctor.common.domin.DoctorEntity;
import com.hss.kgdoctor.mapper.DoctorMapper;
import com.hss.kgdoctor.service.DoctorService;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, DoctorEntity> implements DoctorService {
}
