package com.hss.kgdoctor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.kgdoctor.common.domin.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
