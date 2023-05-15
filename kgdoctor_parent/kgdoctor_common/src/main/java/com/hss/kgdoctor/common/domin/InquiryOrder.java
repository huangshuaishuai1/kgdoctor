package com.hss.kgdoctor.common.domin;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_inquiry_order")
public class InquiryOrder implements Serializable {
    @TableId
    private Long orderId;

    private Long userId;
    private Long doctorId;
    // 0 取消 1 待支付 2 已支付
    private Integer orderStatus;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    // 开始时间也就是支付时间
    private Date startTime;
    private Date endTime;
}
