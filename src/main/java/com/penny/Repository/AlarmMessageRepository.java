package com.penny.Repository;

import com.penny.domain.AlarmMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2017/12/6.
 *报警数据详表接口
 */
public interface AlarmMessageRepository extends JpaRepository<AlarmMessage,String>{

    AlarmMessage findByAlarmCode(Integer alarmCode);

}
