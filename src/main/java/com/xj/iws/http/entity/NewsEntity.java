package com.xj.iws.http.entity;

import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * 消息资料实体类
 *
 * @author Created by XiaoJiang01 on 2017/2/21.
 */
@Alias("NewsEntity")
public class NewsEntity {
    private int id;
    private int alarmId;
    private int userId;
    private int status;
    private Date alarmTime;
    private Date confirmTime;
    private String name;
    private String describes;

    private String userName;

    public NewsEntity() {
    }

    public NewsEntity(int alarmId, String name, String describes) {
        this.alarmId = alarmId;
        this.name = name;
        this.describes = describes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
