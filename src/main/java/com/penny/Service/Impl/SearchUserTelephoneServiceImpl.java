package com.penny.Service.Impl;

import com.penny.Repository.UserDeviceRelationshipRepository;
import com.penny.Repository.UserInfoRepository;
import com.penny.Service.DataSaveService.SearchUserTelephoneService;
import com.penny.Service.DataSaveService.UserInfoService;
import com.penny.domain.UserDeviceRelationship;
import com.penny.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/19.
 * 获取用户手机号码接口
 */
@Service
public class SearchUserTelephoneServiceImpl implements SearchUserTelephoneService,UserInfoService{

    @Autowired
    private UserDeviceRelationshipRepository userDeviceRelationshipRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public List<String> searchUserTelephone(String snCode, Integer isUsed) {

        List<UserDeviceRelationship> UDRList = userDeviceRelationshipRepository.findUserDeviceRelationshipsBySnCodeAndIsUsed(snCode,isUsed);

        List<String> userTelephone = new ArrayList<>();

        for (int i = 0;i<=UDRList.size();i++){

           userTelephone.add(userInfoRepository.findUserInfosByUserName(UDRList.get(i).getUserName()).getUserTelephone());

        }

        return userTelephone;
    }

    @Override
    public UserInfo findUserTelephone(String userName) {
        return null;
    }
}
