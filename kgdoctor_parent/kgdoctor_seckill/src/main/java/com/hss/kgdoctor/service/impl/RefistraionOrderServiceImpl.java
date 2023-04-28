package com.hss.kgdoctor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.kgdoctor.domin.RegistrationOrder;
import com.hss.kgdoctor.mapper.RegistrationMapper;
import com.hss.kgdoctor.service.IRegistrationOrderService;
import org.springframework.stereotype.Service;

@Service
public class RefistraionOrderServiceImpl extends ServiceImpl<RegistrationMapper, RegistrationOrder>  implements IRegistrationOrderService {

}
