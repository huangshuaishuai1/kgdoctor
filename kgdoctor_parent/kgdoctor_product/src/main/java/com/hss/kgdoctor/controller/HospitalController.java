package com.hss.kgdoctor.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hss.kgdoctor.common.domin.HospitalEntity;
import com.hss.kgdoctor.common.web.Result;
import com.hss.kgdoctor.domain.HospitalVO;
import com.hss.kgdoctor.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hospital")
public class HospitalController {

    @Autowired
    HospitalService hospitalService;

    // 分页查询医院信息
    @GetMapping("/list")
    public Result<List<HospitalVO>> listHospital(@RequestParam("size") Integer size, @RequestParam("page") Integer page) {

        page = (page-1)*size;
        List<HospitalEntity> list = hospitalService.getPage(size,page);
        List<HospitalVO> hospitalVOList = list.stream().map(new Function<HospitalEntity, HospitalVO>() {
            @Override
            public HospitalVO apply(HospitalEntity hospitalEntity) {
                HospitalVO hospitalVO = new HospitalVO();
                BeanUtil.copyProperties(hospitalEntity, hospitalVO);
                return hospitalVO;
            }
        }).collect(Collectors.toList());
        return Result.success(hospitalVOList);
    }

    // 根据医院id查找医院详情（包括哪些科室）
    @GetMapping("/{id}")
    public Result<HospitalEntity> getByHospitalId(@PathVariable("id") Integer id) {
        HospitalEntity hospitalEntity = hospitalService.getByHospitalId(id);
        return Result.success(hospitalEntity);
    }

//    @GetMapping("/db/{id}")
//    public Result<HospitalEntity> getByHospitalIdDB(@PathVariable("id") Integer id) {
//        HospitalEntity hospitalEntity = hospitalService.getByHospitalIdDB(id);
//        return Result.success(hospitalEntity);
//    }

}
