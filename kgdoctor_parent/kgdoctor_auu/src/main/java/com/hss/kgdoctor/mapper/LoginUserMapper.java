package com.hss.kgdoctor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.kgdoctor.domin.LoginUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginUserMapper extends BaseMapper<LoginUser> {
}
