package com.penny.Service.DataSaveService;

import com.penny.domain.HistoricalData;

/**
 * Created by Penny on 2017/11/15.
 * 设备是实时数据存储服务接口
 */
public interface HistoricalDataService {

    HistoricalData saveHistoricalData(HistoricalData historicalData);

}
