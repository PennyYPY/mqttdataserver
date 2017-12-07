package com.penny.Service.Impl;

import com.penny.Repository.HistoricalDataRepository;
import com.penny.Service.DataSaveService.HistoricalDataService;
import com.penny.domain.HistoricalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Penny on 2017/11/15.
 * 设备在线数据存储服务实现类
 */
@Service
public class HistoricalDataServiceImpl implements HistoricalDataService {


    @Autowired
    HistoricalDataRepository historicalDataRepository;

    @Override
    public HistoricalData saveHistoricalData(HistoricalData historicalData) {
        return historicalDataRepository.save(historicalData);
    }
}
