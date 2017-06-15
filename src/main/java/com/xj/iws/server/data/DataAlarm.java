package com.xj.iws.server.data;

import com.xj.iws.common.util.ByteUtil;
import com.xj.iws.http.dao.AlarmDao;
import com.xj.iws.http.dao.DeviceDao;
import com.xj.iws.http.dao.NewsDao;
import com.xj.iws.http.dao.PointRoleDao;
import com.xj.iws.http.entity.AlarmEntity;
import com.xj.iws.http.entity.NewsEntity;
import com.xj.iws.http.entity.PointFieldEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by XiaoJiang01 on 2017/3/16.
 */
@Component
public class DataAlarm {

    @Autowired
    DeviceDao deviceDao;
    @Autowired
    AlarmDao alarmDao;
    @Autowired
    PointRoleDao pointRoleDao;
    @Autowired
    NewsDao newsDao;


    private int deviceId;
    private List<PointFieldEntity> pointFields;
    List<Map<String, String>> status;

    public DataAlarm() {
    }

    public DataAlarm(DeviceDao deviceDao, AlarmDao alarmDao, PointRoleDao pointRoleDao, NewsDao newsDao, int deviceId, List<PointFieldEntity> pointFields, List<Map<String, String>> status) {
        this.deviceDao = deviceDao;
        this.alarmDao = alarmDao;
        this.pointRoleDao = pointRoleDao;
        this.newsDao = newsDao;
        this.deviceId = deviceId;
        this.pointFields = pointFields;
        this.status = status;
    }

    public DataAlarm create(String port, String number) {
        this.deviceId = deviceDao.getId(port, number);
        int termId = deviceDao.getTermId(deviceId);
        pointFields = deviceDao.getPointField(termId);
        status = pointRoleDao.getStatus(0);
        return new DataAlarm(deviceDao, alarmDao, pointRoleDao, newsDao, deviceId, pointFields, status);
    }

    public int start(String[] data, String ex) {
        ex = DataFormat.preData(ex, 2, 0);
        String[] exception = DataFormat.subData(ex, 2);
        StringBuffer describes = new StringBuffer();
        AlarmEntity alarm = alarmDao.getAddress(deviceId);

        for (int i = 0; i < exception.length; i++) {
            int number = Integer.parseInt(exception[i]);
            PointFieldEntity field = pointFields.get(number);

            String name = field.getName();
            int roleId = field.getRoleId();

            describes.append(name + ":");
            switch (roleId) {
                case 1:
                    describes.append(role01(data[number], field));
                    break;
                case 2:
                    describes.append(role02(data[number]));
                    break;
                case 3:
                    describes.append(role03(data[number]));
                    break;
                case 6:
                    describes.append(role06(data[number], field));
                    break;
                default:
                    break;
            }
            describes.append("/");
        }

        String name = alarm.getSystemName() + alarm.getLocationName() + alarm.getRoomName() + "#监测异常";
        alarm.setName(name);
        alarm.setDescribes(describes.toString());
        alarmDao.add(alarm);
        addNews(alarm);
        return alarm.getId();
    }

    private void addNews(AlarmEntity alarm) {
        String name = alarm.getSystemName() + "#异常信息";
        String describes = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " " + alarm.getSystemName() + " " + alarm.getLocationName() + " " + alarm.getRoomName() + ":" + alarm.getDescribes();
        NewsEntity news = new NewsEntity(alarm.getId(), name, describes);
        newsDao.add(news);
    }

    public void end(int id) {
        alarmDao.updateEndTime(id);
    }

    private String role01(String s, PointFieldEntity field) {
        double value = (double) Integer.parseInt(s, 16) / field.getMultiple();
        String error = String.valueOf(value) + field.getUnit() + "(" + field.getMin() + field.getUnit() + "~" + field.getMax() + field.getUnit() + ")";
        return error;
    }

    private String role02(String s) {
        StringBuffer value = new StringBuffer();
        s = ByteUtil.hexToBinary(s);
        s = s.substring(8, 16);
        char[] point = s.toCharArray();
        for (int j = 0; j < point.length; j++) {
            if (point[j] == '1') {
                value.append(status.get(1).get(String.valueOf(j + 1)) + " ");
            }
        }
        return value.toString();
    }

    private String role03(String s) {
        StringBuffer value = new StringBuffer();
        s = ByteUtil.hexToBinary(s);
        s = s.substring(10, 16);
        char[] point = s.toCharArray();
        for (int j = 0; j < point.length; j++) {
            if (point[j] == '1') {
                value.append(status.get(2).get(String.valueOf(j + 1)) + " ");
            }
        }
        return value.toString();
    }

    private String role06(String s, PointFieldEntity field) {
        double value = (double) Float.intBitsToFloat(Integer.parseInt(s, 16));
        String error = String.valueOf(value) + field.getUnit() + "(" + field.getMin() + field.getUnit() + "~" + field.getMax() + field.getUnit() + ")";
        return error;
    }
}
