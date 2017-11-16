package com.penny.Service.Impl;


import com.penny.Repository.DevVerifyDataRepository;
import com.penny.Service.DataSaveService.DevVerifyDataService;
import com.penny.domain.DevVerifyData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Penny on 2017/11/15.
 * 设备校验服务实现类
 */
@Service
public class DevVerifyDataServiceImpl implements DevVerifyDataService {

    @Autowired
    private DevVerifyDataRepository devVerifyDataRepository;

    @Override
    public DevVerifyData saveDevVerifyData(DevVerifyData devVerifyData) {
        return devVerifyDataRepository.save(devVerifyData);
    }
}
