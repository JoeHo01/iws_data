package com.xj.iws.http.entity;

import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * Created by XiaoJiang01 on 2017/4/13.
 */
@Alias("DataEntity")
public class DataEntity {
    private int id;
    private int deviceId;
    private String port;
    private String number;
    private Date time;
    private String error;
    private int count;
    private int bit;
    private String data;

    private String strTime;

    public DataEntity() {
    }

    public DataEntity(int id, int deviceId, String port, String number, String time, String error, int count, int bit, String data) {
        this.id = id;
        this.deviceId = deviceId;
        this.port = port;
        this.number = number;
        this.strTime = time;
        this.error = error;
        this.count = count;
        this.bit = bit;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getBit() {
        return bit;
    }

    public void setBit(int bit) {
        this.bit = bit;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStrTime() {
        return strTime;
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }
}
