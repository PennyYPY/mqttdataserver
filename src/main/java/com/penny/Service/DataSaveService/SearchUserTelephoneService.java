package com.penny.Service.DataSaveService;


import java.util.List;

/**
 * Created by Administrator on 2017/12/19.
 */
public interface SearchUserTelephoneService {

    List<String> searchUserTelephone(String snCode, Integer isUsed);

}
