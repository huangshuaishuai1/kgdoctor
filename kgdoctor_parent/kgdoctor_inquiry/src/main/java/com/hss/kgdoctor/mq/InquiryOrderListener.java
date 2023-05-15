package com.hss.kgdoctor.mq;

import com.hss.kgdoctor.common.domin.DoctorEntity;
import com.hss.kgdoctor.common.domin.InquiryOrder;
import com.hss.kgdoctor.common.web.CodeMsg;
import com.hss.kgdoctor.common.web.Result;
import com.hss.kgdoctor.feign.DoctorFeignClient;
import com.hss.kgdoctor.service.InquiryService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(consumerGroup = "inquiryOrderGroup", topic = MQConstants.INJUIRY_ORDER_TOPIC)
public class InquiryOrderListener implements RocketMQListener<InquiryOrder> {

    @Autowired
    InquiryService inquiryService;

    @Autowired
    DoctorFeignClient doctorFeignClient;
    @Override
    public void onMessage(InquiryOrder inquiryOrder) {


        inquiryService.createOrder(inquiryOrder);
    }
}
