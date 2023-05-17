package com.hss.kgdoctor.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalVO {
    private Integer hospitalId;
    private String hospitalName;
    private String hospitalAddress;
}
