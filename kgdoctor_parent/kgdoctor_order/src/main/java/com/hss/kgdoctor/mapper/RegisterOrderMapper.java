package com.hss.kgdoctor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.kgdoctor.common.domin.RegisterEntity;
import com.hss.kgdoctor.domin.RegisterOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegisterOrderMapper extends BaseMapper<RegisterOrder> {

}
