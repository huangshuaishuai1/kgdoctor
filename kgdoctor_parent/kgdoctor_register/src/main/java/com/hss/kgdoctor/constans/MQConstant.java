package com.hss.kgdoctor.constans;

/**
 * Created by lanxw
 */
public class MQConstant {
    //订单队列
    public static final String REGISTER_ORDER_PEDDING_TOPIC = "REGISTER_ORDER_PEDDING_TOPIC";
    //订单结果
    public static final String REGISTER_ORDER_RESULT_TOPIC = "ORDER_RESULT_TOPIC";
    //取消本地标识
    public static final String CANCEL_SECKILL_OVER_SIGE_TOPIC = "CANCEL_SECKILL_OVER_SIGE_TOPIC";
    //订单创建成功Tag
    public static final String REGISTER_ORDER_RESULT_SUCCESS_TAG = "SUCCESS";
    //订单创建成失败Tag
    public static final String REGISTER_ORDER_RESULT_FAIL_TAG = "FAIL";

}
