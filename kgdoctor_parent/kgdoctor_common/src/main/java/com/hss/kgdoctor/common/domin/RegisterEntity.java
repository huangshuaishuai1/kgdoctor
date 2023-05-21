package com.hss.kgdoctor.common.domin;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_register")
public class RegisterEntity {
    @TableId
    Integer registerId;
    // 医生
    Integer registerDoctor;
    // 库存
    Integer registerCount;
    // 日期
    Date registerDate;
}
