package com.hss.kgdoctor.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BindVo implements Serializable {
    private Integer hospitalId;

    private List<Integer> departmentIds;
}
