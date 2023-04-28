package com.hss.kgdoctor.filters;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hss.kgdoctor.common.constants.CommonConstants;
import com.hss.kgdoctor.common.domin.UserDTO;
import com.hss.kgdoctor.common.redis.CommonRedisKey;
import com.hss.kgdoctor.common.util.UserHolder;
import jdk.nashorn.internal.ir.CallNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.hss.kgdoctor.common.redis.CommonRedisKey.USER_TOKEN;

/**
 * Created by hss
 * 定义全局过滤器，功能如下:
 * 1.在请求头中添加FEIGN_REQUEST的请求头，值为0，标记请求不是Feign调用，而是客户端调用
 * 2.刷新Token的有效时间
 */
@Component
@Slf4j
public class CommonFilter implements GlobalFilter {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static AntPathMatcher matcher = new AntPathMatcher();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        /**
         * 2.在请求头中添加FEIGN_REQUEST的请求头，值为0，标记请求不是Feign调用，而是客户端调用
         */
        ServerHttpRequest request = exchange.getRequest().mutate().
                header(CommonConstants.REAL_IP,exchange.getRequest().getRemoteAddress().getHostString()).
                header(CommonConstants.FEIGN_REQUEST_KEY,CommonConstants.FEIGN_REQUEST_FALSE).
                build();
        if(needLogin(request.getPath().toString())){
            return chain.filter(exchange);
        }
        String token=request.getHeaders().getFirst(CommonConstants.TOKEN_NAME);
        if(StrUtil.isBlank(token)){
            token = request.getQueryParams().getFirst(CommonConstants.TOKEN_NAME);
        }
        if(StrUtil.isBlank(token)){
            log.info("【登录拦截】未获取到token值...");
            loginResponse(exchange);
        }
        log.debug("【登录拦截】获取token值:"+token);
        try {
            // 根据token拿到登录用户
            Map<Object, Object> userMap = redisTemplate.opsForHash().entries(USER_TOKEN + token);
            if (userMap.isEmpty()){
                // 进行拦截
                log.info("【登录拦截】token过期...");
                loginResponse(exchange);
            }
            UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);
            // 获取token中的用户名
            Long userId = userDTO.getUserId();
            log.info("【登录拦截】获取userId:"+userId);
            UserHolder.saveUser(userDTO);
            // 刷新token有效期
            redisTemplate.expire(USER_TOKEN + token, 30, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("【登录拦截】异常:"+e.getMessage());
            return loginResponse(exchange);
        }
        return chain.filter(exchange);
    }

    public static Mono<Void> loginResponse(ServerWebExchange exchange) {
        JSONObject json = JSONUtil.createObj();
        json.set("code", 401);
        json.set("msg", "请重新登陆授权");
        ServerHttpResponse response = exchange.getResponse();
        byte[] bytes = JSONUtil.toJsonStr(json).getBytes(StandardCharsets.UTF_8);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Flux.just(buffer));
    }

    private boolean needLogin(String uri) {
        // test
        List<String> uriList = new ArrayList<>();
        uriList.add("/token");
//        uriList.add("/order/list");

        for (String pattern : uriList) {
            if (matcher.match(pattern, uri)) {
                // 不需要拦截
                return true;
            }
        }
        return false;

    }
}
