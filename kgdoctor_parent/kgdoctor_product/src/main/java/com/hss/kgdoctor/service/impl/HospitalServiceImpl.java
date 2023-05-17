package com.hss.kgdoctor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.kgdoctor.common.domin.HospitalEntity;
import com.hss.kgdoctor.mapper.HospitalMapper;
import com.hss.kgdoctor.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hss.kgdoctor.config.RedisConstants.HOSPITAL_DETAIL_KEY;

@Service
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, HospitalEntity> implements HospitalService {
    @Autowired
    HospitalMapper hospitalMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Override
    public List<HospitalEntity> getPage(Integer size, Integer page) {
        return hospitalMapper.page(page, size);
    }

    @Override
    public HospitalEntity getByHospitalId(Integer id) {
//         先查询缓存
        String redisKey = HOSPITAL_DETAIL_KEY + id;
        String hospitalStr = redisTemplate.opsForValue().get(redisKey);
        if (StrUtil.isNotBlank(hospitalStr)) {
            HospitalEntity hospitalEntity = JSONUtil.toBean(hospitalStr, HospitalEntity.class);
            return hospitalEntity;
        }
        // 缓存中没有，从DB中查询，并存放到Redis中
        HospitalEntity hospitalEntity  = hospitalMapper.HospitalDetailById(id);
        String jsonStr = JSONUtil.toJsonStr(hospitalEntity);
        redisTemplate.opsForValue().set(redisKey,jsonStr);
        return hospitalEntity;
    }

    @Override
    public HospitalEntity getByHospitalIdDB(Integer id) {
//         先查询缓存
//        String redisKey = HOSPITAL_DETAIL_KEY + id;
//        String hospitalStr = redisTemplate.opsForValue().get(redisKey);
//        if (StrUtil.isNotBlank(hospitalStr)) {
//            HospitalEntity hospitalEntity = JSONUtil.toBean(hospitalStr, HospitalEntity.class);
//            return hospitalEntity;
//        }
        // 缓存中没有，从DB中查询，并存放到Redis中
        HospitalEntity hospitalEntity  = hospitalMapper.HospitalDetailById(id);
//        String jsonStr = JSONUtil.toJsonStr(hospitalEntity);
//        redisTemplate.opsForValue().set(redisKey,jsonStr);
        return hospitalEntity;
    }
}
