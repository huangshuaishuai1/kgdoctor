package com.hss.kgdoctor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.kgdoctor.domin.RegisterOrder;
import com.hss.kgdoctor.mapper.RegisterOrderMapper;
import com.hss.kgdoctor.service.RegisterOrderService;
import org.springframework.stereotype.Service;

@Service
public class RegisterOrderServiceImpl extends ServiceImpl<RegisterOrderMapper, RegisterOrder> implements RegisterOrderService {
}
