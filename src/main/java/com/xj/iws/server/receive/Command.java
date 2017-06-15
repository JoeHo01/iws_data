package com.xj.iws.server.receive;

import com.xj.iws.common.util.ByteUtil;
import com.xj.iws.common.util.SortUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaoJiang01 on 2017/4/12.
 */
public class Command {
    private int termId;
    private String number;
    private int count;
    private int type;

    private String code;

    private List<Integer> address = new ArrayList<>();
    private List<Integer> target = new ArrayList<>();

    public Command() {
    }

    public Command(String number, int count, int type, int termId) {
        this.number = number;
        this.count = count;
        this.type = type;
        this.termId = termId;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public List<Integer> getAddress() {
        return address;
    }

    public List<Integer> getTarget() {
        return target;
    }

    public void setAddress(List<String> add) {
        int[] intAdd = new int[add.size()];
        this.address = new ArrayList<>();
        for (int i = 0; i < add.size(); i++) {
            intAdd[i] = Integer.parseInt(add.get(i));
            this.address.add(intAdd[i]);
        }
        for (int i = 0; i < intAdd.length; i++) {
            this.target.add((intAdd[i] - SortUtil.min(intAdd)) / 2);
        }
    }

    public void autoCode() {
        String hexNumber = ByteUtil.intToHexString(Integer.parseInt(number), false);
        String hexCount = ByteUtil.intToHexString(count, false);
        this.code = hexNumber.substring(6, 8) + "03" + "0000" + hexCount.substring(4, 8);
    }

    public void manualCode(int field, int status) {
        String hexNumber = ByteUtil.intToHexString(Integer.parseInt(number), false);
        String hexField = ByteUtil.intToHexString(field, false);
        String hexStatus = ByteUtil.intToHexString(status, false);
        this.code = hexNumber.substring(6, 8) + "06" + hexField.substring(4, 8) + hexStatus.substring(4, 8);
    }

    public void addressCode() {
        int[] temp = new int[address.size()];
        for (int i = 0; i < address.size(); i++) {
            temp[i] = address.get(i);
        }
        int min = SortUtil.min(temp);
        int max = SortUtil.max(temp);

        int total = max - min + 2;
        int intAddress = min - 1;

        String hexNumber = ByteUtil.intToHexString(Integer.parseInt(number), false);
        String hexTotal = ByteUtil.intToHexString(total, false);
        String hexAddress = ByteUtil.intToHexString(intAddress, false);

        this.code = hexNumber.substring(6, 8) + "03" + hexAddress.substring(4, 8) + hexTotal.substring(4, 8);
    }
}
