package com.penny.Repository;

import com.penny.domain.DevAlarmData;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2017/11/14.
 * 设备报警数据接口
 */
public interface DevAlarmDataRepository extends JpaRepository<DevAlarmData,String> {
}
