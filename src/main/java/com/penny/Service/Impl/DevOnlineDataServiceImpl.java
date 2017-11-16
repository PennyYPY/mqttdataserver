package com.penny.Service.Impl;

import com.penny.Repository.DevOnlineDataRepository;
import com.penny.Service.DataSaveService.DevOnlineDataService;
import com.penny.domain.DevOnlineData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Penny on 2017/11/15.
 * 设备在线数据服务实现类
 */
@Service
public class DevOnlineDataServiceImpl implements DevOnlineDataService{


    @Autowired
    DevOnlineDataRepository devOnlineDataRepository;

    @Override
    public DevOnlineData saveDevOnlineData(DevOnlineData devOnlineData) {
        return devOnlineDataRepository.save(devOnlineData);
    }
}
