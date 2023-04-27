package com.hss.kgdoctor.domin;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_stock")
public class Stock implements Serializable {
    @TableId
    Long stockId;

    Long doctorId;

    Long stockNum;

    Date date;

}
