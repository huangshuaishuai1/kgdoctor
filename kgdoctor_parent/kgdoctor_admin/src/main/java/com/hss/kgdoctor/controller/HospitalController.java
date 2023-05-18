package com.hss.kgdoctor.controller;

import com.hss.kgdoctor.common.domin.HospitalEntity;
import com.hss.kgdoctor.common.util.AppException;
import com.hss.kgdoctor.common.util.AppExceptionCodeMsg;
import com.hss.kgdoctor.common.web.Resp;
import com.hss.kgdoctor.domin.BindVo;
import com.hss.kgdoctor.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hss.kgdoctor.common.util.AppExceptionCodeMsg.BIND_HOSPITAL_DEPARTMENT_FAIL;

@RestController
@RequestMapping("/hospital")
public class HospitalController {

    @Autowired
    HospitalService hospitalService;

    // 新增医院
    @PostMapping("/add")
    public Resp<String> add(@RequestBody HospitalEntity hospitalEntity) {
        boolean save = hospitalService.save(hospitalEntity);
        if (!save) {
            throw new AppException(AppExceptionCodeMsg.ADD_HOSPITAL_FAIL);
        }
        return Resp.success("default");
    }

    // 修改医院
    @PutMapping("/update")
    public Resp<String> update(@RequestBody HospitalEntity hospitalEntity) {
        boolean b = hospitalService.updateById(hospitalEntity);
        if (!b) {
            throw new AppException(AppExceptionCodeMsg.UPDATE_HOSPITAL_FAIL);
        }
        return Resp.success("default");
    }

    // 为医院绑定科室
    @Transactional
    @PostMapping("/bind")
    public Resp<String> bind(@RequestBody BindVo bindVo) {
        Integer hospitalId = bindVo.getHospitalId();
        List<Integer> departmentIds = bindVo.getDepartmentIds();
        Boolean isSuccess = hospitalService.bind(hospitalId, departmentIds);
        if (!isSuccess) {
            throw new AppException(BIND_HOSPITAL_DEPARTMENT_FAIL);
        }
        return Resp.success("default");
    }

}
