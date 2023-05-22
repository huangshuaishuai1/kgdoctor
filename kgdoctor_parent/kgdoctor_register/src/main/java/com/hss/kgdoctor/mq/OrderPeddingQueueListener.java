package com.hss.kgdoctor.mq;


import com.hss.kgdoctor.common.util.AppException;
import com.hss.kgdoctor.constans.MQConstant;
import com.hss.kgdoctor.feign.OrderClient;
import com.hss.kgdoctor.feign.RegisterClient;
import com.hss.kgdoctor.service.SeckillService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static com.hss.kgdoctor.common.util.AppExceptionCodeMsg.REPEAT_DO_SECKILL;
import static com.hss.kgdoctor.common.util.AppExceptionCodeMsg.SECKILL_FAIL;
import static com.hss.kgdoctor.constans.RedisKey.SECKILL_ORDER_SET;


@Component
@RocketMQMessageListener(consumerGroup = "penddingGroup",topic = MQConstant.REGISTER_ORDER_PEDDING_TOPIC)
public class OrderPeddingQueueListener implements RocketMQListener<OrderMessage> {

//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    SeckillService seckillService;

    @Autowired
    RegisterClient registerClient;

    @Autowired
    OrderClient orderClient;

    @Autowired
    RedissonClient redissonClient;
    @Override
    public void onMessage(OrderMessage orderMessage) {
        Integer userId = orderMessage.getUserId();
        Integer registerId = orderMessage.getRegisterId();

        //获取分布式锁
        RLock lock = redissonClient.getLock("seckill:doseckill:lock" + userId);
        boolean isLock = lock.tryLock();
        if (!isLock) {
            // 代表挂号失败，需要回补预库存
            seckillService.syncStockToRedis(registerId);
            throw new AppException(REPEAT_DO_SECKILL);
        }
        try {
            // 库存减一
            registerClient.decrStock(registerId);
            // 创建订单
            orderClient.createOrder(userId,registerId);
            // 将该用户放入redis的set集合中，防止其重复下单
            String repeatKey = SECKILL_ORDER_SET + registerId;
            redisTemplate.opsForSet().add(repeatKey,String.valueOf(userId));
        } catch (Exception e) {
            // 代表挂号失败，需要回补预库存
            seckillService.syncStockToRedis(registerId);
            throw new AppException(SECKILL_FAIL);
        }finally {
            lock.unlock();
        }
    }
}
