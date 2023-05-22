package com.hss.kgdoctor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.kgdoctor.common.domin.DoctorVO;
import com.hss.kgdoctor.common.util.JwtHelper;
import com.hss.kgdoctor.common.web.CodeMsg;
import com.hss.kgdoctor.common.web.Result;
import com.hss.kgdoctor.domin.RegistrationOrder;
import com.hss.kgdoctor.domin.Stock;
import com.hss.kgdoctor.feign.DoctorFeignClient;
import com.hss.kgdoctor.mapper.StockMapper;
import com.hss.kgdoctor.service.IRegistrationOrderService;
import com.hss.kgdoctor.service.IStockService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.hss.kgdoctor.common.redis.CommonRedisKey.USER_TOKEN;
import static com.hss.kgdoctor.constants.RedisKeyConstants.*;

@Service
@Slf4j
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements IStockService {


    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    DoctorFeignClient doctorFeignClient;

    @Autowired
    IRegistrationOrderService registrationOrderService;
    @Autowired
    RedissonClient redissonClient;

    /**
     * 上传单个挂号的商品
     * @param id
     * @return
     */
    @Override
    public String uploadById(Long id) {
        // 查出id对应的日期
        Stock stock = this.getById(id);
        Date date = stock.getDate();
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate startDate = getStartTime();
        LocalDate endDate = getEndTime(2);

        // 判断日期是否是今天、明天、后天  （不是的就不需要手动上传，因为之后的会有定时任务去上传）
        // 不是，返回
        if (localDate.isAfter(endDate) || localDate.isBefore(startDate)) {
            return "失败:不符合时间！";
        }
        // 是，判断该id是否已经在缓存中了
        String infoKey = SKEKILL_DOCTOR_KEY + date.getTime();
        String stockKey = SKEKILL_STOCK_KEY + date.getTime();
        Boolean hasKey = stringRedisTemplate.opsForHash().hasKey(infoKey, String.valueOf(id));
        // 是，直接返回
        if (hasKey) {
            return "失败:请勿重复上传！";
        }
        // 根据id获取doctor的信息
        Long doctorId = stock.getDoctorId();
        Result doctorInfoWithSpecialtyById = doctorFeignClient.getDoctorInfoWithSpecialtyById(doctorId);
        Object data =  doctorInfoWithSpecialtyById.getData();
        if (data != null) {
            DoctorVO doctorVO = BeanUtil.toBean(data, DoctorVO.class);
            String jsonStr = JSONUtil.toJsonStr(doctorVO);
            // 不是，上传信息和库存到Redis中

            stringRedisTemplate.opsForHash().put(infoKey,String.valueOf(id),jsonStr);
            stringRedisTemplate.opsForHash().put(stockKey,String.valueOf(id),String.valueOf(stock.getStockNum()));
            return "成功:上传成功";
        }else {
            return "失败:未找到医生信息";
        }

    }

    /**
     * 获取所有在指定时间内的库存
     * @return
     */
    public List<Stock> getStockByTime() {
        LocalDate startTime = getStartTime();
        String s = startTime.toString();
        LocalDate endTime = getEndTime(3);
        String e = endTime.toString();
        QueryWrapper<Stock> wrapper = new QueryWrapper<Stock>().between("date", s, e);
        return this.list(wrapper);
    }

    /**
     * 将库存和挂号的专家信息上传到Redis中
     * @return
     */
    @Async
    @Scheduled(cron = "0 0 3 * * ?")
    public void uploadByTime() {
        RLock lock = redissonClient.getLock("seckill:upload:lock");
        lock.lock(10, TimeUnit.SECONDS);
        try {
            log.info("开始删除过期商品");
            deleteByTime();
            log.info("开始上传！！");
            List<Stock> stocks = getStockByTime();
            for (Stock stock : stocks) {
                // 上传每个stock对应的专家信息和库存信息到Redis中
                String msg = uploadById(stock.getStockId());
                if ("失败".equals(msg.substring(0,2))) {
                    String[] split = msg.split(":");
                    log.info("秒杀商品" + stock.getStockId() + split[1]);
                }
            }
        }finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional
    public Result doSeckill(Long id, String token) {
        Stock stock = getById(id);
        if (stock == null) {
            CodeMsg notExist = new CodeMsg(5000, "商品不存在");
            return Result.error(notExist);
        }
        // 判断时间是否到了八点钟，没到就不准抢(比如后天的挂号是今天8点开始可抢的)
        if (isVaildTime(stock.getDate())) {
            CodeMsg timeError = new CodeMsg(5001, "未到抢购时间");
            return Result.error(timeError);
        }

        // 判断是否登录，没登陆的不准抢
        if (token == null || Boolean.FALSE.equals(stringRedisTemplate.hasKey(USER_TOKEN + token))){
            CodeMsg needLogin = new CodeMsg(5002, "请登陆后再操作");
            return Result.error(needLogin);
        }
        String jwt = stringRedisTemplate.opsForValue().get(USER_TOKEN + token);
//        UserDTO user = UserHolder.getUser();
        Integer userId = JwtHelper.getUserId(jwt);
        if (StrUtil.isBlank(jwt) || userId == null) {
            CodeMsg needLogin = new CodeMsg(5002, "请登陆后再操作");
            return Result.error(needLogin);
        }
        // 判断是否下过单了，不可重复下单
        String repeatKey = SECKILL_ORDER_SET + id;
        if (Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(repeatKey, String.valueOf(userId)))){
            CodeMsg repeatOperate = new CodeMsg(5004, "请勿重复挂号！");
            return Result.error(repeatOperate);
        }

//        // 判断库存数量，数量不足返回失败
//        Long stockNum = stock.getStockNum();
//        if (stockNum <= 0) {
//            CodeMsg sold = new CodeMsg(5003, "商品已经卖完啦");
//            return Result.error(sold);
//        }

        // 这里使用Redis中的预库存进行一次拦截
        String countKey = SKEKILL_STOCK_KEY + stock.getDate().getTime();
        Long remainCount = stringRedisTemplate.opsForHash().increment(countKey, String.valueOf(id), -1);
        if (remainCount < 0) {
            CodeMsg sold = new CodeMsg(5003, "该医生当天已越满");
            return Result.error(sold);
        }

        // 获取分布式锁
        RLock lock = redissonClient.getLock("seckill:doseckill:lock" + userId);
        boolean isLock = lock.tryLock();
        if (!isLock) {
            CodeMsg repeatOperate = new CodeMsg(5004, "请勿重复挂号！");
            return Result.error(repeatOperate);
        }
        try {
            // ·向order表插入数据
            boolean isSuccess = creatOrder(stock,userId);
            // 如果秒杀失败了，要对Redis中的预库存进行恢复
            if (!isSuccess) {
                CodeMsg sold = new CodeMsg(5003, "商品已经卖完啦");
                // 恢复库存
                syncRedisStock(id);
                return Result.error(sold);
            }
        }finally {
            lock.unlock();
        }
        // 将挂号成功的用户id加入Redis中
        stringRedisTemplate.opsForSet().add(repeatKey,String.valueOf(userId));
        return Result.success();
    }

    private boolean isVaildTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 把日期往后增加一天,整数  往后推,负数往前移动
        calendar.add(Calendar.DATE, -1);
        calendar.add(Calendar.HOUR, -16);
        date = calendar.getTime();
        Date now = new Date();
        return now.getTime() < date.getTime();
    }

    public void syncRedisStock(Long seckillId) {
        Stock stock = this.getById(seckillId);
        Long stockNum =  stock.getStockNum();
        if(stockNum>0){
            String key = SKEKILL_STOCK_KEY + stock.getDate().getTime();
            stringRedisTemplate.opsForHash().put(key,String.valueOf(seckillId),String.valueOf(stockNum));
        }
    }


    private boolean creatOrder(Stock stock, Integer userId) {
        RegistrationOrder registrationOrder = new RegistrationOrder();
        registrationOrder.setUserId(userId);
        registrationOrder.setDoctorId(stock.getDoctorId());
        registrationOrder.setOrderStatus(1);
        registrationOrder.setRegistrationDate(stock.getDate());
        registrationOrder.setStockId(stock.getStockId());
        registrationOrderService.save(registrationOrder);
        // 减对应商品库存(乐观锁的思想)
        boolean isSuccess = this.update().setSql("stock_num = stock_num-1")
                .eq("stock_id", stock.getStockId())
                .gt("stock_num", 0)
                .update();
        return isSuccess;
    }

    private void deleteByTime() {
        // 因为是三点钟执行的，删除昨天的商品就行
        LocalDate time = getEndTime(-1);
        Date date = Date.from(time.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());;
        String infoKey = SKEKILL_DOCTOR_KEY + date.getTime();
        String stockKey = SKEKILL_STOCK_KEY + date.getTime();
        stringRedisTemplate.delete(infoKey);
        stringRedisTemplate.delete(stockKey);
    }

    public
    static LocalDate getStartTime() {
        return LocalDate.now();
    }
    static LocalDate getEndTime(Integer days) {
        LocalDate now = LocalDate.now();
        return now.plusDays(2);
    }

}
