package com.hss.kgdoctor.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterVO implements Serializable {

    Integer doctorId;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    Date date;
    Integer count;
}
