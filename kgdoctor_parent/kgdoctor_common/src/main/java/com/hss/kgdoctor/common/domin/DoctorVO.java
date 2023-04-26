package com.hss.kgdoctor.common.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorVO implements Serializable {

    private String doctorName;

    private String hospital;

    private String title;

    private String introduction;

    private String specialtyName;
}
