package com.hss.kgdoctor.mq;


import com.hss.kgdoctor.common.util.AppException;
import com.hss.kgdoctor.constans.MQConstant;
import com.hss.kgdoctor.feign.OrderClient;
import com.hss.kgdoctor.feign.RegisterClient;
import com.hss.kgdoctor.service.SeckillService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static com.hss.kgdoctor.common.util.AppExceptionCodeMsg.SECKILL_FAIL;


@Component
@RocketMQMessageListener(consumerGroup = "penddingGroup",topic = MQConstant.REGISTER_ORDER_PEDDING_TOPIC)
public class OrderPeddingQueueListener implements RocketMQListener<OrderMessage> {

//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    SeckillService seckillService;

    @Autowired
    RegisterClient registerClient;

    @Autowired
    OrderClient orderClient;
    @Override
    public void onMessage(OrderMessage orderMessage) {
        Integer userId = orderMessage.getUserId();
        Integer registerId = orderMessage.getRegisterId();
        try{
            // 库存减一
            registerClient.decrStock(registerId);
            // 创建订单
            orderClient.createOrder(userId,registerId);
        }catch (Exception e) {
            // 代表挂号失败，需要回补预库存
            seckillService.syncStockToRedis(registerId);
            throw new AppException(SECKILL_FAIL);
        }
    }
}
