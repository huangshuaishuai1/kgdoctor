package com.hss.kgdoctor.common.domin;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tb_doctor")
public class DoctorEntity implements Serializable {
    @TableId
    private Long doctorId;

    private String doctorName;

    private String hospital;

    private String title;

    private String introduction;

    private Long userId;

}
