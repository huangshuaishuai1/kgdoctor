package com.hss.kgdoctor.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MQOrderMessage implements Serializable {

    private Long userId;
    private Long doctorId;
}
