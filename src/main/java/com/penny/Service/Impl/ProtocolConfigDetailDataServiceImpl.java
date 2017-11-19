package com.penny.Service.Impl;

import com.penny.Repository.ProtocolConfigDetailDataRepository;
import com.penny.Service.DataSaveService.ProtocolConfigDetailDataService;
import com.penny.domain.ProtocolConfigDetailData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Penny on 2017/11/15.
 * 设备协议配置实现类
 */

@Service
public class ProtocolConfigDetailDataServiceImpl implements ProtocolConfigDetailDataService{

    @Autowired
    private ProtocolConfigDetailDataRepository protocolConfigDetailDataRepository;

    @Override
    public ProtocolConfigDetailData saveProtocolConfigData(ProtocolConfigDetailData protocolConfigDetailData) {
        return protocolConfigDetailDataRepository.save(protocolConfigDetailData);
    }
}
