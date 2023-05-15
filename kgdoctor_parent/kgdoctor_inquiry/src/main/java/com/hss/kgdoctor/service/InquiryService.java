package com.hss.kgdoctor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.kgdoctor.common.domin.InquiryOrder;
import com.hss.kgdoctor.common.web.Result;

public interface InquiryService extends IService<InquiryOrder> {
    Result buyInquiry(Long doctorId, String token);
}
