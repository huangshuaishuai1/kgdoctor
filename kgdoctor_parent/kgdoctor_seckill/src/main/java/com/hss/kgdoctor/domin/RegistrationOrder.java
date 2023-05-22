package com.hss.kgdoctor.domin;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_registration_order")
public class RegistrationOrder {
    @TableId
    Long orderId;

    Integer userId;

    Long doctorId;

    Long stockId;

    Date RegistrationDate;

    Integer orderStatus;

    @TableField(fill = FieldFill.INSERT)
    Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    Date updateTime;
}
