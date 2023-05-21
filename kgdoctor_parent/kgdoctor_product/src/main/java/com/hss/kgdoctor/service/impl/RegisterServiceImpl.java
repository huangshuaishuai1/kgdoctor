package com.hss.kgdoctor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.kgdoctor.common.domin.DoctorVO;
import com.hss.kgdoctor.common.domin.RegisterEntity;
import com.hss.kgdoctor.common.util.AppException;
import com.hss.kgdoctor.common.web.Resp;
import com.hss.kgdoctor.common.web.Result;
import com.hss.kgdoctor.domain.DoctorDTO;
import com.hss.kgdoctor.mapper.RegisterMapper;
import com.hss.kgdoctor.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.hss.kgdoctor.common.util.AppExceptionCodeMsg.REPEAT_UPLOAD_EXCEPTION;
import static com.hss.kgdoctor.common.util.AppExceptionCodeMsg.UPLOAD_TIME_INVALID;
import static com.hss.kgdoctor.constants.RedisKeyConstants.SKEKILL_STOCK_KEY;

@Service
@Slf4j
public class RegisterServiceImpl extends ServiceImpl<RegisterMapper,RegisterEntity> implements RegisterService{
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;
    public static LocalDate getStartTime() {
        return LocalDate.now();
    }
    static LocalDate getEndTime(Integer days) {
        LocalDate now = LocalDate.now();
        return now.plusDays(2);
    }

    private void deleteByTime() {
        // 因为是三点钟执行的，删除昨天的商品就行
        LocalDate time = getEndTime(-1);
        Date date = Date.from(time.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());;
        String stockKey = SKEKILL_STOCK_KEY + date.getTime();
        stringRedisTemplate.delete(stockKey);
    }
    @Override
    public Resp uploadById(Integer id) {
        // 查出id对应的日期
        RegisterEntity register = this.getById(id);
        Date date = register.getRegisterDate();
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate startDate = getStartTime();
        LocalDate endDate = getEndTime(2);

        // 判断日期是否是今天、明天、后天  （不是的就不需要手动上传，因为之后的会有定时任务去上传）
        // 不是，返回
        if (localDate.isAfter(endDate) || localDate.isBefore(startDate)) {
            throw new AppException(UPLOAD_TIME_INVALID);
        }
        // 是，判断该id是否已经在缓存中了
        String stockKey = SKEKILL_STOCK_KEY + date.getTime();
        Boolean hasKey = stringRedisTemplate.opsForHash().hasKey(stockKey,String.valueOf(id));
        // 是，直接返回
        if (hasKey) {
            throw new AppException(REPEAT_UPLOAD_EXCEPTION);
        }
        // 上传库存
        stringRedisTemplate.opsForHash().put(stockKey,String.valueOf(id),String.valueOf(register.getRegisterCount()));

        return Resp.success("上传成功！");
    }

    public List<RegisterEntity> getStockByTime() {
        LocalDate startTime = getStartTime();
        String s = startTime.toString();
        LocalDate endTime = getEndTime(3);
        String e = endTime.toString();
        QueryWrapper<RegisterEntity> wrapper = new QueryWrapper<RegisterEntity>().between("register_date", s, e);
        return this.list(wrapper);
    }

    // 定时任务
    @Async
    @Scheduled(cron = "0 0 3 * * ?")
    public void uploadByTime() {
        RLock lock = redissonClient.getLock("seckill:upload:lock");
        lock.lock(10, TimeUnit.SECONDS);
        try {
            log.info("开始删除过期商品");
            deleteByTime();
            log.info("开始上传！！");
            List<RegisterEntity> stocks = getStockByTime();
            for (RegisterEntity stock : stocks) {
                // 上传每个stock对应的专家信息和库存信息到Redis中
                uploadById(stock.getRegisterId());
            }
        }finally {
            lock.unlock();
        }
    }

}
