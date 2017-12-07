package com.penny.Service.Impl;

import com.penny.Repository.AlarmMessageRepository;
import com.penny.Service.DataSaveService.AlarmMessageService;
import com.penny.domain.AlarmMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/12/6.
 * 查找报警详情实现类
 */
@Service
public class AlarmMessageServiceImpl implements AlarmMessageService{

    @Autowired
    AlarmMessageRepository alarmMessageRepository;


    @Override
    public AlarmMessage findAlarmInfo(Integer alarmCode) {
        return alarmMessageRepository.findByAlarmCode(alarmCode);
    }
}
