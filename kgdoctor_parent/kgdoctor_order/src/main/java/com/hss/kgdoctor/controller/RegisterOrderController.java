package com.hss.kgdoctor.controller;

import com.hss.kgdoctor.common.util.AppException;
import com.hss.kgdoctor.common.web.Resp;
import com.hss.kgdoctor.domin.RegisterOrder;
import com.hss.kgdoctor.service.RegisterOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hss.kgdoctor.common.util.AppExceptionCodeMsg.STOCK_OUT_OF_COUNT;

@RestController
@RequestMapping("/order")
public class RegisterOrderController {

    @Autowired
    RegisterOrderService registerOrderService;
    // 根据用户Id和挂号id创建订单
    @PostMapping("/register/{userId}/{registerId}")
    public Resp createOrder(@PathVariable("userId") Integer userId, @PathVariable("registerId") Integer registerId) {
        RegisterOrder registerOrder = new RegisterOrder();
        registerOrder.setRegisterId(registerId);
        registerOrder.setUserId(userId);
        registerOrder.setOrderStatus(1);
        boolean save = registerOrderService.save(registerOrder);
        if (!save) {
            throw new AppException(STOCK_OUT_OF_COUNT);
        }
        return Resp.success("创建订单成功");
    }
}
