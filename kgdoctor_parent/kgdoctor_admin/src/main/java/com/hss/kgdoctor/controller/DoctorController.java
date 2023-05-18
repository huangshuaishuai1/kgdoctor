package com.hss.kgdoctor.controller;


import cn.hutool.json.JSONUtil;
import com.hss.kgdoctor.common.domin.DoctorEntity;
import com.hss.kgdoctor.common.util.AppException;
import com.hss.kgdoctor.common.web.Resp;
import com.hss.kgdoctor.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static com.hss.kgdoctor.common.util.AppExceptionCodeMsg.*;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    DoctorService doctorService;

    // 新增医生
    @PostMapping("/add")
    public Resp<String> add(@RequestBody DoctorEntity doctorEntity) {
        boolean save = doctorService.save(doctorEntity);
        if (!save) {
            throw new AppException(ADD_DOCTOR_FAIL);
        }
        return Resp.success("success");
    }

    // 根据邮箱号为医生绑定用户账号
    @PutMapping("/bindUser/{doctorId}")
    public Resp<String> bindUser(@PathVariable("doctorId") Integer doctorId, @RequestBody String emailJson) {
        DoctorEntity doctorEntity = new DoctorEntity();
        doctorEntity.setDoctorId(doctorId);
        HashMap bean = JSONUtil.toBean(emailJson, HashMap.class);
        String email = (String) bean.get("email");
        Integer userId = doctorService.getUserIdByEmail(email);
        if (userId == null) {
            throw new AppException(ACCOUNT_NOT_EXIT);
        }
        doctorEntity.setUserId(userId);
        boolean isSuccess = doctorService.updateById(doctorEntity);
        if (!isSuccess) {
            throw new AppException(BIND_USER_FAIL);
        }
        return Resp.success("default");
    }

    // 为医生开启/禁止在线问诊
    @PutMapping("/enableInquiry/{doctorId}")
    public Resp<String> enableInquiry(@PathVariable("doctorId") Integer doctorId) {
        DoctorEntity doctor = doctorService.getById(doctorId);
        if (doctor == null) {
            throw new AppException(DCOTOR_NOT_EXIT);
        }
        Integer tag = doctor.getEnableInquiry();
        if (tag == 0) {
            doctor.setEnableInquiry(1);
        }else {
            doctor.setEnableInquiry(0);
        }
        boolean isSuccess = doctorService.updateById(doctor);
        if (!isSuccess) {
            throw new AppException(INQUIRY_CHANGE_FAIL);
        }
        return Resp.success("success");
    }
}
