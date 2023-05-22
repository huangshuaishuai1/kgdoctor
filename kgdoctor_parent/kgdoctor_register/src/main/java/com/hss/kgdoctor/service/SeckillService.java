package com.hss.kgdoctor.service;

import com.hss.kgdoctor.common.web.Resp;
import com.hss.kgdoctor.common.web.Result;

public interface SeckillService {
    Resp doSeckill(Integer id, String token);

    void syncStockToRedis(Integer registerId);
}
