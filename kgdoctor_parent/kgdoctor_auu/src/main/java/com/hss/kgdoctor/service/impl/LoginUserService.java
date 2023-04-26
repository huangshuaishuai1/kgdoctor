package com.hss.kgdoctor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.kgdoctor.common.domin.UserEntity;
import com.hss.kgdoctor.domin.LoginUser;
import com.hss.kgdoctor.mapper.LoginUserMapper;
import com.hss.kgdoctor.mapper.UserMapper;
import com.hss.kgdoctor.service.ILoginUserService;
import com.hss.kgdoctor.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class LoginUserService extends ServiceImpl<LoginUserMapper, LoginUser> implements ILoginUserService {

}
