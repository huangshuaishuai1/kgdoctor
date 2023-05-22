package com.hss.kgdoctor.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.kgdoctor.common.domin.RegisterEntity;
import com.hss.kgdoctor.common.web.Resp;

public interface RegisterService extends IService<RegisterEntity> {
    Resp uploadById(Integer id);

    public void uploadByTime();

    void decrStock(Integer registerId);
}
