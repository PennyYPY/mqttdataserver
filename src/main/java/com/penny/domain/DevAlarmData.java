package com.penny.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Penny on 2017/11/13.
 * 设备报警表
 */
@Entity
@Table(name = "device_alarm_data")
public class DevAlarmData {

    @Id
    @GeneratedValue
    private String id;
    private String alarmPayload;
    private String alarmTopic;
    private Date alarmTime;
    private String dataName;
    private BigDecimal dataValue;
    private Date dealTime;
    private Integer isDeal;
    private Integer offset;
    private String protocolVersion;
    private String snCode;
    private String transactor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlarmPayload() {
        return alarmPayload;
    }

    public void setAlarmPayload(String alarmPayload) {
        this.alarmPayload = alarmPayload;
    }

    public String getAlarmTopic() {
        return alarmTopic;
    }

    public void setAlarmTopic(String alarmTopic) {
        this.alarmTopic = alarmTopic;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public BigDecimal getDataValue() {
        return dataValue;
    }

    public void setDataValue(BigDecimal dataValue) {
        this.dataValue = dataValue;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public int getIsDeal() {
        return isDeal;
    }

    public void setIsDeal(int isDeal) {
        this.isDeal = isDeal;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getSnCode() {
        return snCode;
    }

    public void setSnCode(String snCode) {
        this.snCode = snCode;
    }

    public String getTransactor() {
        return transactor;
    }

    public void setTransactor(String transactor) {
        this.transactor = transactor;
    }
}
