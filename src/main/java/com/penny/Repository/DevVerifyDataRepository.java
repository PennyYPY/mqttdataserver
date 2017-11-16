package com.penny.Repository;

import com.penny.domain.DevVerifyData;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2017/11/14.
 *
 * 设备校验接口
 */
public interface DevVerifyDataRepository extends JpaRepository<DevVerifyData,String>{
}
