package com.hss.kgdoctor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hss.kgdoctor.common.domin.DoctorEntity;
import com.hss.kgdoctor.common.domin.RegisterEntity;
import com.hss.kgdoctor.common.web.Resp;
import com.hss.kgdoctor.domain.RegisterVO;
import com.hss.kgdoctor.service.DoctorService;
import com.hss.kgdoctor.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.hss.kgdoctor.common.util.AppExceptionCodeMsg.DCOTOR_NOT_EXIT;
import static com.hss.kgdoctor.common.util.AppExceptionCodeMsg.REPEAT_CREATE_REGISTER;

@RestController
@RequestMapping("/setRegister")
public class RegisterController {
    @Autowired
    DoctorService doctorService;

    @Autowired
    RegisterService registerService;


    // 根据医生ID和日期创建挂号
    @PostMapping("/add")
    public Resp<String> addRegister(@RequestBody RegisterVO registerVO) {
        Integer doctorID = registerVO.getDoctorId();
        // 得到Doctor对象
        DoctorEntity doctor = doctorService.getById(doctorID);
        // 医生不存在
        if (doctor == null) {
            return Resp.error(DCOTOR_NOT_EXIT);
        }
        // 重复创建
        QueryWrapper<RegisterEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("register_doctor",doctorID).eq("register_doctor",registerVO.getDate());
        if (registerService.getOne(wrapper) != null) {
            return Resp.error(REPEAT_CREATE_REGISTER);
        }
        RegisterEntity registerEntity = new RegisterEntity();
        registerEntity.setRegisterCount(registerVO.getCount());
        registerEntity.setRegisterDoctor(doctorID);
        registerEntity.setRegisterDate(registerVO.getDate());
        registerService.save(registerEntity);
        return Resp.success("default");
    }

    // 根据医生ID查询挂号的信息（要是今天之后的）
    @GetMapping("/find/{doctorId}")
    public Resp<RegisterEntity> findRegisterByDoctorId(@PathVariable("doctorId") Integer doctorId) {
        QueryWrapper<RegisterEntity> wrapper = new QueryWrapper<>();
//        Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        wrapper.eq("register_id",doctorId);
        wrapper.apply("date_format(register_date,'%y%m%d') >= date_format('" +LocalDate.now()+"','%y%m%d')");
        List<RegisterEntity> list = registerService.list(wrapper);
        return Resp.success(list);
    }
}
