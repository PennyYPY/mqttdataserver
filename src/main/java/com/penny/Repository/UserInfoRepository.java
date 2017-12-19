package com.penny.Repository;

import com.penny.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator on 2017/12/19.
 */
public interface UserInfoRepository extends JpaRepository<UserInfo,String>{

    UserInfo findUserInfosByUserName(String userName);

}
