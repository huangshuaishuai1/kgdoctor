package com.hss.kgdoctor.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO implements Serializable {

    private String doctorName;

    private String hospitalName;

    private String departmentName;

    private String title;

    private String introduction;


}
