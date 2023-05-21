package com.hss.kgdoctor.service.impl;

import cn.hutool.core.util.StrUtil;
import com.hss.kgdoctor.common.domin.RegisterEntity;
import com.hss.kgdoctor.common.util.AppException;
import com.hss.kgdoctor.common.util.JwtHelper;
import com.hss.kgdoctor.common.web.CodeMsg;
import com.hss.kgdoctor.common.web.Resp;
import com.hss.kgdoctor.common.web.Result;
import com.hss.kgdoctor.constans.MQConstant;
import com.hss.kgdoctor.mapper.RegisterMapper;
import com.hss.kgdoctor.mq.OrderMessage;
import com.hss.kgdoctor.service.SeckillService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.redisson.api.RLock;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.hss.kgdoctor.common.redis.CommonRedisKey.USER_TOKEN;
import static com.hss.kgdoctor.common.util.AppExceptionCodeMsg.*;
import static com.hss.kgdoctor.constans.RedisKey.SECKILL_ORDER_SET;
import static com.hss.kgdoctor.constans.RedisKey.SKEKILL_STOCK_KEY;

@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    RegisterMapper registerMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RocketMQTemplate rocketMQTemplate;
    @Override
    @Transactional
    public Resp doSeckill(Integer id, String token) {
        RegisterEntity registerEntity = registerMapper.getById(id);
        if (registerEntity == null) {
            throw new AppException(PRODUCT_NOT_EXITS);
        }
        // 判断时间是否到了八点钟，没到就不准抢(比如后天的挂号是今天8点开始可抢的)
        if (isVaildTime(registerEntity.getRegisterDate())) {
            throw new AppException(SECKILL_TIME_EXCEPTION);
        }

        // 判断是否登录，没登陆的不准抢
        if (token == null || Boolean.FALSE.equals(stringRedisTemplate.hasKey(USER_TOKEN + token))) {
            throw new AppException(UNLOGIN);
        }
        String jwt = stringRedisTemplate.opsForValue().get(USER_TOKEN + token);
//        UserDTO user = UserHolder.getUser();
        Long userId = JwtHelper.getUserId(jwt);
        if (StrUtil.isBlank(jwt) || userId == null) {
            throw new AppException(UNLOGIN);
        }
        // 判断是否下过单了，不可重复下单
        String repeatKey = SECKILL_ORDER_SET + id;
        if (Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(repeatKey, String.valueOf(userId)))) {
            throw new AppException(REPEAT_DO_SECKILL);
        }

        // 这里使用Redis中的预库存进行一次拦截
        String countKey = SKEKILL_STOCK_KEY + registerEntity.getRegisterDate().getTime();
        Long remainCount = stringRedisTemplate.opsForHash().increment(countKey, String.valueOf(id), -1);
        if (remainCount < 0) {
            throw new AppException(STOCK_OUT_OF_COUNT);
        }
            // 这里通过消息队列传递出去
        OrderMessage orderMessage = new OrderMessage(token, id);
        rocketMQTemplate.syncSend(MQConstant.ORDER_PEDDING_TOPIC,orderMessage);
        return Resp.success("成功进入秒杀队列,请耐心等待结果");
//                // 获取分布式锁
//        RLock lock = redissonClient.getLock("seckill:doseckill:lock" + userId);
//        boolean isLock = lock.tryLock();
//        if (!isLock) {
//            throw new AppException(REPEAT_DO_SECKILL);
//        }
//        try {
//        } finally {
//            lock.unlock();
//        }

    }

    private boolean isVaildTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 把日期往后增加一天,整数  往后推,负数往前移动
        calendar.add(Calendar.DATE, -1);
        calendar.add(Calendar.HOUR, -16);
        date = calendar.getTime();
        Date now = new Date();
        return now.getTime() < date.getTime();

    }



}
