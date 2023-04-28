package com.hss.kgdoctor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.kgdoctor.common.domin.UserEntity;
import com.hss.kgdoctor.common.exception.BusinessException;
import com.hss.kgdoctor.common.redis.CommonRedisKey;
import com.hss.kgdoctor.common.util.JwtHelper;
import com.hss.kgdoctor.domin.LoginUser;
import com.hss.kgdoctor.common.domin.UserDTO;
import com.hss.kgdoctor.mapper.UserMapper;
import com.hss.kgdoctor.redis.UaaRedisKey;
import com.hss.kgdoctor.service.IUserService;
import com.hss.kgdoctor.util.MD5Util;
import com.hss.kgdoctor.web.msg.UAACodeMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.hss.kgdoctor.common.redis.CommonRedisKey.USER_TOKEN;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    LoginUserService loginUserService;

    @Override
    public String login(String email, String password) {
        LoginUser loginUser = this.getUser(email);
        //进行密码加盐比对
        if(loginUser==null || !loginUser.getPassword().equals(MD5Util.encode(password,loginUser.getSalt()))){
            throw new BusinessException(UAACodeMsg.LOGIN_ERROR);
        }
        UserEntity userInfo = this.getUserInfo(email);
        String token = UUID.randomUUID().toString().replace("-","");
        String jwt = JwtHelper.createToken(userInfo.getUserId(), userInfo.getUserName());
        String key = USER_TOKEN + token;
//        UserDTO userDTO = new UserDTO();
//        BeanUtil.copyProperties(userInfo,userDTO);
//        Map<String, Object> beanMap = BeanUtil.beanToMap(userDTO);
//        Map<String, String> stringMap = new HashMap<>();
//        beanMap.forEach((k,v) -> {
//            stringMap.put(k,String.valueOf(v));
//        });
        redisTemplate.opsForValue().set(key,jwt,30,TimeUnit.MINUTES);
        return token;
    }

    private LoginUser getUser(String email) {
        LoginUser loginUser;
        String userLoginHashKey = UaaRedisKey.USERLOGIN_HASH.getRealKey("");
        String userInfoHashKey = UaaRedisKey.USERINFO_HASH.getRealKey("");
        String zSetKey = UaaRedisKey.USER_ZSET.getRealKey("");
        String objStr = (String) redisTemplate.opsForHash().get(userLoginHashKey, email);
        if(StrUtil.isEmpty(objStr)){
            //缓存中并没有，从数据库中查询
            QueryWrapper<LoginUser> wrapper = new QueryWrapper<>();
            wrapper.eq("email",email);
            loginUser = loginUserService.getOne(wrapper);

            //把用户的登录信息存储到Hash结构中.
            if(loginUser!=null){
                QueryWrapper<UserEntity> queryWrapperr = new QueryWrapper<>();
                queryWrapperr.eq("email",email);
                UserEntity userInfo = this.getOne(queryWrapperr);
                redisTemplate.opsForHash().put(userInfoHashKey, email, JSONUtil.toJsonStr(userInfo));
                redisTemplate.opsForHash().put(userLoginHashKey, email,JSONUtil.toJsonStr(loginUser));
            }
            //使用zSet结构,value存用户手机号码，分数为登录时间，在定时器中找出7天前登录的用户，然后再缓存中删除.
            //我们缓存中的只存储7天的用户登录信息(热点用户)
        }else{
            //缓存中有这个key
            loginUser = JSONUtil.toBean(objStr,LoginUser.class);
        }
        redisTemplate.opsForZSet().add(zSetKey, email,new Date().getTime());
        return loginUser;

    }

    @Override
    public UserEntity getUserInfo(String email) {
        QueryWrapper<UserEntity> queryWrapperr = new QueryWrapper<>();
        queryWrapperr.eq("email",email);
        return this.getOne(queryWrapperr);
    }

//    private String createToken(String email) {
//        //token创建
//        String token = UUID.randomUUID().toString().replace("-","");
//        //把user对象存储到redis中
//        CommonRedisKey user_token_key = CommonRedisKey.USER_TOKEN;
//        redisTemplate.opsForValue().set(user_token_key.getRealKey(token), email, user_token_key.getExpireTime(),user_token_key.getUnit());
//        return token;
//    }
}
