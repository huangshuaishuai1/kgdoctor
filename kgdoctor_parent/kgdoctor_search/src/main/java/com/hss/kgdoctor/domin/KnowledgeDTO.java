package com.hss.kgdoctor.domin;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_knowledge")
public class KnowledgeDTO implements Serializable {
    // 知识ID
    @TableId
    private Integer knowledgeId;
    // 知识类型
    private Integer knowledgeType;
    //知识内容
    private String knowledgeContent;
}
