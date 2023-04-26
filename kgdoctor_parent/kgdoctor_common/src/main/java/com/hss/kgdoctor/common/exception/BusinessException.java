package com.hss.kgdoctor.common.exception;

import com.hss.kgdoctor.common.web.CodeMsg;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by hss
 */
@Setter
@Getter
public class BusinessException extends RuntimeException {
    private CodeMsg codeMsg;
    public BusinessException(CodeMsg codeMsg){
        this.codeMsg = codeMsg;
    }
}
