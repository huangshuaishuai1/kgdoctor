package com.hss.kgdoctor.common.domin;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_department")
public class Department {
    private Integer departmentId; // 科室id
    private String departmentName; // 科室名称
}
