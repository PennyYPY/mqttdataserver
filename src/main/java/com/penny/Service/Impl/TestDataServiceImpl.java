package com.penny.Service.Impl;

import com.penny.Repository.TestDataRepository;
import com.penny.Service.DataSaveService.TestDataService;
import com.penny.domain.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Penny on 2017/11/13.
 * 测试数据服务实现类
 */
@Service
public class TestDataServiceImpl implements TestDataService{

    @Autowired
    private TestDataRepository testDataRepository;


    @Override
    public TestData saveData(TestData testData) {
        return testDataRepository.save(testData);
    }
}
