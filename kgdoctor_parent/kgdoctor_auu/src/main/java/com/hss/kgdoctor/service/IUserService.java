package com.hss.kgdoctor.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hss.kgdoctor.common.domin.UserEntity;

/**
 * Created by hss
 */
public interface IUserService extends IService<UserEntity> {
    /**
     * 登录功能
     * @param email 用户的账号/用户的手机号码
     * @param password 用户的密码
     * @param
     * @return
     */
    String login(String email, String password);

    /**
     * 根据邮箱获取UserInfo
     * @param email
     * @return
     */
    UserEntity getUserInfo(String email);
}
