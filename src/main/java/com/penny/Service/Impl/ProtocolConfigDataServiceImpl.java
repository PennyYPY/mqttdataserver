package com.penny.Service.Impl;

import com.penny.Repository.ProtocolConfigDataRepository;
import com.penny.Service.DataSaveService.ProtocolConfigDataService;
import com.penny.domain.ProtocolConfigDetailData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Penny on 2017/11/15.
 * 设备协议配置实现类
 */

@Service
public class ProtocolConfigDataServiceImpl implements ProtocolConfigDataService{

    @Autowired
    private ProtocolConfigDataRepository protocolConfigDataRepository;

    @Override
    public ProtocolConfigDetailData saveProtocolConfigData(ProtocolConfigDetailData protocolConfigDetailData) {
        return protocolConfigDataRepository.save(protocolConfigDetailData);
    }
}
