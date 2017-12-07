package com.penny.Service.DataSaveService;

import com.penny.domain.AlarmMessage;
import com.penny.domain.ProtocolConfigMasterData;

/**
 * Created by Administrator on 2017/12/6.
 * 报警详细信息服务类
 */
public interface AlarmMessageService {

    AlarmMessage findAlarmInfo(Integer alarmCode);

}
