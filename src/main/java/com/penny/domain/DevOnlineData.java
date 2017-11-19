package com.penny.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by Penny on 2017/11/13.
 *
 * 设备实时数据表
 */
@Entity
@Table(name = "device_online_data")
public class DevOnlineData {

    @Id
    private String id;
    private String snCode;
    private String protocolVersion;
    private Integer offset;
    private BigDecimal dataValue;

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

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public BigDecimal getDataValue() {
        return dataValue;
    }

    public void setDataValue(BigDecimal dataValue) {
        this.dataValue = dataValue;
    }
}
