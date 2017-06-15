package com.xj.iws.http.entity;

import org.apache.ibatis.type.Alias;

/**
 * Created by XiaoJiang01 on 2017/3/24.
 */
@Alias("PointRoleEntity")
public class PointRoleEntity {
    private int id;
    private String protocol;
    private int type;
    private int bit;
    private String name;
    private String describes;
    private String bit01;
    private String bit02;
    private String bit03;
    private String bit04;
    private String bit05;
    private String bit06;
    private String bit07;
    private String bit08;


    public PointRoleEntity() {
    }

    public PointRoleEntity(int id, String protocol, int type, int bit, String name, String describes, String bit01, String bit02, String bit03, String bit04, String bit05, String bit06, String bit07, String bit08) {
        this.id = id;
        this.protocol = protocol;
        this.type = type;
        this.bit = bit;
        this.name = name;
        this.describes = describes;
        this.bit01 = bit01;
        this.bit02 = bit02;
        this.bit03 = bit03;
        this.bit04 = bit04;
        this.bit05 = bit05;
        this.bit06 = bit06;
        this.bit07 = bit07;
        this.bit08 = bit08;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBit() {
        return bit;
    }

    public void setBit(int bit) {
        this.bit = bit;
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

    public String getBit01() {
        return bit01;
    }

    public void setBit01(String bit01) {
        this.bit01 = bit01;
    }

    public String getBit02() {
        return bit02;
    }

    public void setBit02(String bit02) {
        this.bit02 = bit02;
    }

    public String getBit03() {
        return bit03;
    }

    public void setBit03(String bit03) {
        this.bit03 = bit03;
    }

    public String getBit04() {
        return bit04;
    }

    public void setBit04(String bit04) {
        this.bit04 = bit04;
    }

    public String getBit05() {
        return bit05;
    }

    public void setBit05(String bit05) {
        this.bit05 = bit05;
    }

    public String getBit06() {
        return bit06;
    }

    public void setBit06(String bit06) {
        this.bit06 = bit06;
    }

    public String getBit07() {
        return bit07;
    }

    public void setBit07(String bit07) {
        this.bit07 = bit07;
    }

    public String getBit08() {
        return bit08;
    }

    public void setBit08(String bit08) {
        this.bit08 = bit08;
    }

}
