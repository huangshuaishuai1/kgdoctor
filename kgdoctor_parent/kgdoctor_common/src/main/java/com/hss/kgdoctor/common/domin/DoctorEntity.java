package com.hss.kgdoctor.common.domin;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_doctor")
public class DoctorEntity implements Serializable {
    @TableId
    private Integer doctorId;

    private String doctorName;

    private Integer hospitalId;

    private Integer departmentId;

    private String title;

    private String introduction;

    private Integer userId;

    // 是否开启接诊  0 关闭  1  开启
    private Integer enableInquiry;

}
