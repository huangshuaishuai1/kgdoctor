package com.hss.kgdoctor.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.kgdoctor.common.domin.DoctorEntity;
import com.hss.kgdoctor.common.domin.InquiryOrder;
import com.hss.kgdoctor.common.util.JwtHelper;
import com.hss.kgdoctor.common.web.CodeMsg;
import com.hss.kgdoctor.common.web.Result;
import com.hss.kgdoctor.feign.DoctorFeignClient;
import com.hss.kgdoctor.mapper.InquiryMapper;
import com.hss.kgdoctor.mq.MQOrderMessage;
import com.hss.kgdoctor.service.InquiryService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.hss.kgdoctor.common.redis.CommonRedisKey.USER_TOKEN;
import static com.hss.kgdoctor.mq.MQConstants.INJUIRY_ORDER_TOPIC;

@Service
public class InquiryServiceImpl extends ServiceImpl<InquiryMapper, InquiryOrder> implements InquiryService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    DoctorFeignClient doctorFeignClient;

    @Autowired
    RocketMQTemplate rocketMQTemplate;
    @Override
    public Result buyInquiry(Long doctorId, String token) {
        // 判断用户是否登录
        if (StrUtil.isBlank(token) || Boolean.FALSE.equals(stringRedisTemplate.hasKey(USER_TOKEN + token))) {
            CodeMsg needLogin = new CodeMsg(5002, "请登陆后再操作");
            return Result.error(needLogin);
        }
        String jwt = stringRedisTemplate.opsForValue().get(USER_TOKEN + token);
        Long userId = JwtHelper.getUserId(jwt);
        if (StrUtil.isBlank(jwt) || userId == null) {
            CodeMsg needLogin = new CodeMsg(5002, "请登陆后再操作");
            return Result.error(needLogin);
        }        if (!doctorFeignClient.isExist(doctorId)) {
            CodeMsg doctorNotExist = new CodeMsg(7001, "医生不存在！");
            return Result.error(doctorNotExist);
//            return;
        }
        Result<DoctorEntity> doctorInfoById = doctorFeignClient.getDoctorInfoById(doctorId);
        DoctorEntity doctor = doctorInfoById.getData();
        // 判断医生是否接诊
        if (doctor.getEnableInquiry() == 0) {
            CodeMsg doctorNotInquiry = new CodeMsg(7002, "医生暂不接诊！");
            return Result.error(doctorNotInquiry);
//            return;
        }
        InquiryOrder inquiryOrder = new InquiryOrder();
        inquiryOrder.setOrderStatus(0);
        inquiryOrder.setDoctorId(doctorId);
        inquiryOrder.setUserId(userId);


        rocketMQTemplate.syncSend(INJUIRY_ORDER_TOPIC,inquiryOrder);
//        createOrder(inquiryOrder);
        return Result.success("创建订单中...");
    }


    public boolean createOrder(InquiryOrder inquiryOrder) {

        return save(inquiryOrder);
    }
}
