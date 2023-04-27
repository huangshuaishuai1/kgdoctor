package com.hss.kgdoctor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hss.kgdoctor.domin.Stock;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface StockMapper extends BaseMapper<Stock> {
//    List<Stock> getStockByTime(String startTime, String endTime);
}
