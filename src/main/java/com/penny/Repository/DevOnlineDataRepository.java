package com.penny.Repository;

import com.penny.domain.DevOnlineData;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2017/11/14.
 *
 * 设备实时数据接口
 */
public interface DevOnlineDataRepository extends JpaRepository<DevOnlineData,String> {


}
