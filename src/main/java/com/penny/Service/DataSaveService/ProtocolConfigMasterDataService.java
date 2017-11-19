package com.penny.Service.DataSaveService;

import com.penny.domain.ProtocolConfigMasterData;

import java.util.List;

/**
 * Created by Penny on 2017/11/15.
 * 协议配置主表数据服务接口
 */
public interface ProtocolConfigMasterDataService {

   ProtocolConfigMasterData upDateProtocolConfigMaster(String sn,String protocolVersion,Integer isUsed);

   ProtocolConfigMasterData saveProtocolConfigMasterData(ProtocolConfigMasterData protocolConfigMasterData);

   ProtocolConfigMasterData findProtocolConfigMasterDataBySnCodeAndProtocolVersion(String snCode,String protocolVersion);

}
