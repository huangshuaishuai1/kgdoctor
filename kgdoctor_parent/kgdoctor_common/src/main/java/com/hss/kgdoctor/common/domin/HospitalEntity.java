package com.hss.kgdoctor.common.domin;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_hospital")
public class HospitalEntity {
    @TableId
    private Integer hospitalId; // 医院id
    private String hospitalName; // 医院名称
    private String hospitalAddress; // 医院地址
    @TableField(exist = false)
    private ArrayList<Department> departments; // 科室
}
