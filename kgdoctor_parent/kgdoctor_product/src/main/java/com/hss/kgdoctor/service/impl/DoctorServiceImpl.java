package com.hss.kgdoctor.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.kgdoctor.common.domin.DoctorEntity;
import com.hss.kgdoctor.common.domin.HospitalEntity;
import com.hss.kgdoctor.common.util.AppException;
import com.hss.kgdoctor.domain.DoctorDTO;
import com.hss.kgdoctor.mapper.DoctorMapper;
import com.hss.kgdoctor.service.DepartmentService;
import com.hss.kgdoctor.service.DoctorService;
import com.hss.kgdoctor.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.hss.kgdoctor.common.util.AppExceptionCodeMsg.DCOTOR_NOT_EXIT;
import static com.hss.kgdoctor.constants.RedisKeyConstants.*;

@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, DoctorEntity> implements DoctorService {

    @Autowired
    HospitalService hospitalService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public DoctorDTO getDoctorDetail(Integer doctorId) {
        String redisKey = DOCTOR_DETAIL_KEY + doctorId;
        String json = stringRedisTemplate.opsForValue().get(redisKey);
        // 判断是否是空对象
        if (NULL_OBJECT.equals(json)) {
            throw new AppException(DCOTOR_NOT_EXIT);
        }
        DoctorDTO bean = JSONUtil.toBean(json, DoctorDTO.class);
        if (bean != null) {
            return bean;
        }
        // 重建缓存，缓存控对象解决缓存穿透
        DoctorEntity doctor = this.getById(doctorId);
        if (doctor == null) {
            stringRedisTemplate.opsForValue().set(redisKey,NULL_OBJECT, DOCTOR_DETAIL_NULL_OBJECT_TIMEOUT,TimeUnit.MINUTES);
            throw new AppException(DCOTOR_NOT_EXIT);
        }

        Integer hospitalId = doctor.getHospitalId();
        HospitalEntity hospital = hospitalService.getById(hospitalId);
        String hospitalName = hospital.getHospitalName();
        Integer departmentId = doctor.getDepartmentId();
        String departmentName = departmentService.getById(departmentId).getDepartmentName();
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setTitle(doctor.getTitle());
        doctorDTO.setDoctorName(doctor.getDoctorName());
        doctorDTO.setIntroduction(doctor.getIntroduction());
        doctorDTO.setHospitalName(hospitalName);
        doctorDTO.setDepartmentName(departmentName);
        String jsonStr = JSONUtil.toJsonStr(doctorDTO);
        // 插入缓存
        stringRedisTemplate.opsForValue().set(redisKey,jsonStr,DOCTOR_DETAIL_TIMEOUT, TimeUnit.MINUTES);
        return doctorDTO;
    }
}
