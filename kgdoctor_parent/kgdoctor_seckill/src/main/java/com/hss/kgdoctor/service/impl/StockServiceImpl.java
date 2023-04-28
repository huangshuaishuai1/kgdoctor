package com.hss.kgdoctor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hss.kgdoctor.common.domin.DoctorVO;
import com.hss.kgdoctor.common.web.CodeMsg;
import com.hss.kgdoctor.common.web.Result;
import com.hss.kgdoctor.domin.Stock;
import com.hss.kgdoctor.feign.DoctorFeignClient;
import com.hss.kgdoctor.mapper.StockMapper;
import com.hss.kgdoctor.service.IStockService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.hss.kgdoctor.constants.RedisKeyConstants.SKEKILL_DOCTOR_KEY;
import static com.hss.kgdoctor.constants.RedisKeyConstants.SKEKILL_STOCK_KEY;

@Service
@Slf4j
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements IStockService {


    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    DoctorFeignClient doctorFeignClient;

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
