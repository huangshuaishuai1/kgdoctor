package com.hss.kgdoctor.common.web.interceptor;

import com.hss.kgdoctor.common.constants.CommonConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Created by lanxw
 */
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.header(CommonConstants.FEIGN_REQUEST_KEY,CommonConstants.FEIGN_REQUEST_TRUE);
    }
}
