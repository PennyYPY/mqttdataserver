package com.penny.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
 * Created by Penny on 2017/11/14.
 *
 * 设备校验表
 */
@Entity
@Table(name = "device_verify")
public class DevVerifyData {

    @Id
    private String id;
    private String snCode;
    private String checkCode;
    private Date generateTime;
    private String protocolVersion;
    private String topic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSnCode() {
        return snCode;
    }

    public void setSnCode(String snCode) {
        this.snCode = snCode;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public Date getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(Date generateTime) {
        this.generateTime = generateTime;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
