package com.xj.iws.http.service.impl;

import com.xj.iws.common.util.DataWrapper;
import com.xj.iws.common.util.TimeUtil;
import com.xj.iws.http.dao.DataDao;
import com.xj.iws.http.dao.DeviceDao;
import com.xj.iws.http.dao.mysql.LoadDataInStream;
import com.xj.iws.http.entity.DataEntity;
import com.xj.iws.http.service.DataService;
import com.xj.iws.http.dao.redis.RedisBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2017/6/14.
 */
@Service
public class DataServiceImpl implements DataService {
    @Autowired
    RedisBase redisBase;
    @Autowired
    DataDao dataDao;
    @Autowired
    DeviceDao deviceDao;
    @Autowired
    LoadDataInStream loadDataInStream;

    @Override
    public DataWrapper<Void> saveAll() {

        String date = TimeUtil.getDate(new Date(), -1);
        Set<String> keys = redisBase.setOps().members("keys_device_run");

        for (String key : keys) {
            String port = key.split("#")[0];
            String number = key.split("#")[1];

            Map<String, Integer> dataInfo = deviceDao.getDataInfo(port, number);

            int deviceId = dataInfo.get("deviceId");
            int count = dataInfo.get("count");
            int bit = dataInfo.get("bit");

            String tableName = "data_" + key;

            Set<String> values = redisBase.zSetOps().range("data_" + date + "_" + key,0,-1);
            List<String[]> datas = new ArrayList<>();
            String fieldName = "deviceid,port,number,time,error,bit,count,data";

            for (String value : values) {
                String[] temp = value.split(":");
                String time = date + temp[0];
                String data = temp[1];
                String exception = temp[2];

                String[] field = new String[8];
                field[0] = String.valueOf(deviceId);
                field[1] = port;
                field[2] = number;
                field[3] = time;
                field[4] = exception;
                field[5] = String.valueOf(count);
                field[6] = String.valueOf(bit);
                field[7] = data;

                datas.add(field);
            }
            loadDataInStream.write(tableName, datas, fieldName);
        }
        getRunDevice();
        return null;
    }

    private void getRunDevice() {
        redisBase.getRedisTemplate().delete("keys_device_run");
        Set<String> runningDevices = redisBase.setOps().members("keys_device_running");
        for (String device : runningDevices) {
            redisBase.setOps().add("keys_device_run", device);
        }
    }
}
