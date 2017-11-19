package com.penny.Service.Impl;

import com.penny.Repository.ProtocolConfigMasterDataRepository;
import com.penny.Service.DataSaveService.ProtocolConfigMasterDataService;
import com.penny.domain.ProtocolConfigMasterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Penny on 2017/11/15.
 * 协议配置主表数据服务接口实现类
 */
@Service
public class ProtocolConfigMasterDataServiceImpl implements ProtocolConfigMasterDataService{

    @Autowired
    ProtocolConfigMasterDataRepository protocolConfigMasterDataRepository;


    @Override
    public ProtocolConfigMasterData upDateProtocolConfigMaster(String sn, String protocolVersion, Integer isUsed) {

        ProtocolConfigMasterData protocolConfigMasterData = protocolConfigMasterDataRepository.findProtocolConfigMasterDataBySnCodeAndProtocolVersion(sn,protocolVersion);
        protocolConfigMasterData.setIsUsed(isUsed);
        protocolConfigMasterDataRepository.save(protocolConfigMasterData);

        return null;
    }

    @Override
    public ProtocolConfigMasterData saveProtocolConfigMasterData(ProtocolConfigMasterData protocolConfigMasterData) {
        return protocolConfigMasterDataRepository.save(protocolConfigMasterData);
    }

    @Override
    public ProtocolConfigMasterData findProtocolConfigMasterDataBySnCodeAndProtocolVersion(String snCode, String protocolVersion) {
        return protocolConfigMasterDataRepository.findProtocolConfigMasterDataBySnCodeAndProtocolVersion(snCode,protocolVersion);

    }
}
