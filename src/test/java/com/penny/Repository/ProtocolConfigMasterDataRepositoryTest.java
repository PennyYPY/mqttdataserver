package com.penny.Repository;

import com.penny.domain.ProtocolConfigMasterData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/11/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProtocolConfigMasterDataRepositoryTest {

    @Autowired
    private ProtocolConfigMasterDataRepository repository;

    @Test
    public void findProtocolConfigMasterDataBySnCodeAndProtocolVersion() throws Exception {

        ProtocolConfigMasterData result = repository.findProtocolConfigMasterDataBySnCodeAndProtocolVersion("hmi001","V3.1");
        Assert.assertNotNull(result);
    }

}