package com.penny.Service.Impl;

import com.penny.Repository.DevAlarmDataRepository;
import com.penny.Service.DataSaveService.DevAlarmDataService;
import com.penny.domain.DevAlarmData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Penny on 2017/11/15.
 * 设备报警服务实现类
 */
@Service
public class DevAlarmDataServiceImpl implements DevAlarmDataService{

    @Autowired
    private DevAlarmDataRepository devAlarmDataRepository;

    @Override
    public DevAlarmData saveDevAlarmData(DevAlarmData devAlarmData) {
        return devAlarmDataRepository.save(devAlarmData);
    }
}
