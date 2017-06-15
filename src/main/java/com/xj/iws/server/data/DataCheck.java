package com.xj.iws.server.data;

import com.xj.iws.common.util.ByteUtil;
import com.xj.iws.http.dao.DeviceDao;
import com.xj.iws.http.entity.PointFieldEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by XiaoJiang01 on 2017/3/16.
 */
@Component
public class DataCheck {
    @Autowired
    DeviceDao deviceDao;

    private List<PointFieldEntity> pointFields;

    public DataCheck() {
    }

    public DataCheck(DeviceDao deviceDao, List<PointFieldEntity> pointFields) {
        this.deviceDao = deviceDao;
        this.pointFields = pointFields;
    }

    public DataCheck create(String port, String number) {
        int deviceId = deviceDao.getId(port, number);
        int termId = deviceDao.getTermId(deviceId);
        pointFields = deviceDao.getPointField(termId);
        return new DataCheck(deviceDao,pointFields);
    }

    public String check(String[] data) {
        StringBuffer error = new StringBuffer("ER");
        for (int i = 0; i < pointFields.size(); i++) {
            PointFieldEntity field = pointFields.get(i);
            switch (field.getRoleId()) {
                case 1:
                    error.append(role01(data[i], field, i));
                    break;
                case 2:
                    error.append(role02(data[i], i));
                    break;
                case 3:
                    error.append(role03(data[i], i));
                    break;
                case 6:
                    error.append(role06(data[i], field, i));
                    break;
                default:
                    break;
            }
        }
        return error.toString();
    }

    private String role01(String s, PointFieldEntity field, int i) {
        String error = "";

        double value = (double)Integer.parseInt(s,16) / field.getMultiple();

        if (value < field.getMin() || value > field.getMax()) {
            error = String.format("%02d", i);
        }
        return error;
    }

    private String role02(String s, int i) {
        String error = "";
        s = ByteUtil.hexToBinary(s);
        char[] value = s.toCharArray();
        if (value[5] == '1') {
            error = String.format("%02d", i);
        }
        return error;
    }

    private String role03(String s, int i) {
        String error = "";
        s = ByteUtil.hexToBinary(s);
        char[] value = s.toCharArray();
        for (int j = 0; j < value.length; j++) {
            if (value[j] == '1') {
                error = String.format("%02d", i);
                break;
            }
        }
        return error;
    }

    private String role06(String s, PointFieldEntity field, int i) {
        String error = "";

        double value = Float.intBitsToFloat(Integer.parseInt(s,16));

        if (value < field.getMin() || value > field.getMax()) {
            error = String.format("%02d", i);
        }
        return error;
    }
}
