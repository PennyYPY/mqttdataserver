package com.penny.Service.DataSaveService;

import com.penny.domain.DevAlarmData;

/**
 * Created by Penny on 2017/11/14.
 * 存储报警数据服务接口
 */
public interface DevAlarmDataService {

    DevAlarmData saveDevAlarmData(DevAlarmData devAlarmData);

}
