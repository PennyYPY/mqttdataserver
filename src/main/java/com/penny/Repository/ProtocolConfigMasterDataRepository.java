package com.penny.Repository;

import com.penny.domain.ProtocolConfigMasterData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.integration.dsl.jpa.Jpa;

import java.util.List;

/**
 * Created by Administrator on 2017/11/15.
 */
public interface ProtocolConfigMasterDataRepository extends JpaRepository<ProtocolConfigMasterData,String> {

    ProtocolConfigMasterData findProtocolConfigMasterDataBySnCodeAndProtocolVersion(String snCode,String protocolVersion);

}
