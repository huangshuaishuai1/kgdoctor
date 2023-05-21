package com.hss.kgdoctor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hss.kgdoctor.common.domin.DoctorEntity;
import com.hss.kgdoctor.common.web.Resp;
import com.hss.kgdoctor.domain.DoctorDTO;
import com.hss.kgdoctor.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    DoctorService doctorService;

    // 根据医院ID和科室ID找到对应的医生
    @GetMapping("/{hospitalId}/{departmentId}")
    public Resp<List<DoctorEntity>> getDoctorByIds(@PathVariable("hospitalId") Integer hospitalId, @PathVariable("departmentId") Integer departmentId) {
        QueryWrapper<DoctorEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("hospital_id",hospitalId).eq("department_id",departmentId);
        List<DoctorEntity> list = doctorService.list(wrapper);
        if (list.isEmpty()) {
            return Resp.success("该科室暂无可服务医生！",list);
        }
        return Resp.success(list);
    }

    // 根据ID查找医生的详情信息（这里会尝试从缓存中找）
    @GetMapping(("/detail/{doctorId}"))
    public Resp<DoctorDTO> getDoctorDetail(@PathVariable("doctorId") Integer doctorId) {
        DoctorDTO doctorDTO = doctorService.getDoctorDetail(doctorId);
        return Resp.success(doctorDTO);
    }
}
