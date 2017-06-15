package com.xj.iws.http.entity;

import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * Created by XiaoJiang01 on 2017/3/24.
 */
@Alias("DeviceTermEntity")
public class DeviceTermEntity {
    private int id;
    private int count;
    private String protocol;
    private String type;
    private String name;
    private String describes;

    private List<PointFieldEntity> fields;

    public DeviceTermEntity() {
    }

    public DeviceTermEntity(int id, int count, String protocol, String type, String name, String describes) {
        this.id = id;
        this.count = count;
        this.protocol = protocol;
        this.type = type;
        this.name = name;
        this.describes = describes;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<PointFieldEntity> getFields() {
        return fields;
    }

    public void setFields(List<PointFieldEntity> fields) {
        this.fields = fields;
    }
}
