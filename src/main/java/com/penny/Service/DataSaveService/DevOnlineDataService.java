package com.penny.Service.DataSaveService;

import com.penny.domain.DevOnlineData;

/**
 * Created by Penny on 2017/11/15.
 * 设备是实时数据存储服务接口
 */
public interface DevOnlineDataService {

    DevOnlineData saveDevOnlineData(DevOnlineData devOnlineData);

}
