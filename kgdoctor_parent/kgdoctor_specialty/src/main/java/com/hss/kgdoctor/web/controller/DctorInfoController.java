package com.hss.kgdoctor.web.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hss.kgdoctor.common.domin.DoctorEntity;
import com.hss.kgdoctor.common.domin.DoctorVO;
import com.hss.kgdoctor.common.web.CodeMsg;
import com.hss.kgdoctor.common.web.Result;
import com.hss.kgdoctor.service.IDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

import static com.hss.kgdoctor.constants.RedisKeyConstants.DOCTOR_CACHE_KEY;
import static com.hss.kgdoctor.constants.RedisKeyConstants.DOCTOR_WITH_SPECIALTY_CACHE_KEY;

@RestController
@RequestMapping("/doc")
public class DctorInfoController {

    @Autowired
    IDoctorService doctorService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @GetMapping("/info/{id}")
    public Result getDoctorInfoById(@PathVariable("id") Long id) {
        DoctorEntity info;
        // 先从Redis中找
        String key = DOCTOR_CACHE_KEY + String.valueOf(id);
        String doctorInfoJsonStr = stringRedisTemplate.opsForValue().get(key);
        if (!StrUtil.isBlank(doctorInfoJsonStr)) {
            info = JSONUtil.toBean(doctorInfoJsonStr,DoctorEntity.class);
            return Result.success(info);
        }
        info = doctorService.getDoctorInfoById(id);
        if (info == null) {
            CodeMsg msg = new CodeMsg(200, "医生不存在");
            return Result.error(msg);
        }
        // 将查询到的数据放入Redis中
        String jsonStr = JSONUtil.toJsonStr(info);
        stringRedisTemplate.opsForValue().set(key,jsonStr,60, TimeUnit.MINUTES);
        return Result.success(info);
    }

    @GetMapping("/infowithspe/{id}")
    public Result getDoctorInfoWithSpecialtyById(@PathVariable("id") Long id) {
        DoctorVO info;
        // 先从Redis中找
        String key = DOCTOR_WITH_SPECIALTY_CACHE_KEY + String.valueOf(id);
        String doctorInfoJsonStr = stringRedisTemplate.opsForValue().get(key);
        if (!StrUtil.isBlank(doctorInfoJsonStr)) {
            info = JSONUtil.toBean(doctorInfoJsonStr,DoctorVO.class);
            return Result.success(info);
        }
        info = doctorService.getDoctorInfoWithSpecialtyById(id);
        if (info == null) {
            CodeMsg msg = new CodeMsg(200, "医生不存在");
            return Result.error(msg);
        }
        // 将查询到的数据放入Redis中
        String jsonStr = JSONUtil.toJsonStr(info);
        stringRedisTemplate.opsForValue().set(key,jsonStr,60, TimeUnit.MINUTES);
        return Result.success(info);
    }
}
