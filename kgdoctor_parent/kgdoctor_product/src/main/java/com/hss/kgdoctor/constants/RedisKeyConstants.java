package com.hss.kgdoctor.constants;

public class RedisKeyConstants {

    public static final String SKEKILL_STOCK_KEY = "cache:seckill:doctor:stock:";

    public static final String DOCTOR_DETAIL_KEY = "cache:doctor:detail:";
    public static final Long DOCTOR_DETAIL_TIMEOUT = 30L;
    public static final Long DOCTOR_DETAIL_NULL_OBJECT_TIMEOUT = 3L;

    public static final String NULL_OBJECT = "####";
}
