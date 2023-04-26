package com.hss.kgdoctor.common.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by hss
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CodeMsg implements Serializable {
    private Integer code;
    private String msg;
}
