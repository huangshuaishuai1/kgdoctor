package com.hss.kgdoctor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.kgdoctor.common.web.Result;
import com.hss.kgdoctor.domin.Stock;

public interface IStockService extends IService<Stock> {
    String uploadById(Long id);

    void uploadByTime();

    Result doSeckill(Long id, String token);
}
