package com.xj.iws.http.entity;

import org.apache.ibatis.type.Alias;

/**
 * Created by XiaoJiang01 on 2017/3/24.
 */
@Alias("PointFieldEntity")
public class PointFieldEntity {
    private int id;
    private int termId;
    private int number;
    private int roleId;
    private String name;
    private String describes;
    private String unit;
    private double min;
    private double max;
    private int multiple;

    private DeviceTermEntity table;

    public PointFieldEntity() {
    }

    public PointFieldEntity(int id, int deviceId, int number, int roleId, String name, String describes, String unit, double min, double max, int multiple) {
        this.id = id;
        this.termId = deviceId;
        this.number = number;
        this.roleId = roleId;
        this.name = name;
        this.describes = describes;
        this.unit = unit;
        this.min = min;
        this.max = max;
        this.multiple = multiple;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeviceId() {
        return termId;
    }

    public void setDeviceId(int deviceId) {
        this.termId = deviceId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public DeviceTermEntity getTable() {
        return table;
    }

    public void setTable(DeviceTermEntity table) {
        this.table = table;
    }
}
