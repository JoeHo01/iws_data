package com.xj.iws.server.data;

import com.xj.iws.http.dao.DataDao;
import com.xj.iws.http.dao.DeviceDao;
import com.xj.iws.server.receive.Command;
import com.xj.iws.http.dao.redis.RedisBase;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/6/7.
 */
public class DataTask extends TimerTask {

    RedisBase redisBase;
    DataDao dataDao;
    DeviceDao deviceDao;

    String IP;
    String port;
    List<Command> commands;

    List<DataCheck> checkers;
    List<DataAlarm> alarms;

    //初始化报警信息
    Map<String, Integer> alarmIds = new HashMap<>();
    Map<String, Boolean> alarmLight = new HashMap<>();
    Map<String, Boolean> preLight = new HashMap<>();

    SimpleDateFormat dateForm;
    SimpleDateFormat timeForm;

    public DataTask(RedisBase redisBase, DataDao dataDao, DeviceDao deviceDao, String IP, String port, List<Command> commands, List<DataCheck> checkers, List<DataAlarm> alarms) {
        this.redisBase = redisBase;
        this.dataDao = dataDao;
        this.deviceDao = deviceDao;
        this.IP = IP;
        this.port = port;
        this.commands = commands;
        this.checkers = checkers;
        this.alarms = alarms;
        dateForm = new SimpleDateFormat("yyyyMMdd");
        timeForm = new SimpleDateFormat("HHmmss");
    }

    public void run() {
        //读取数据
        for (int i = 0; i < commands.size(); i++) {
            read(i);
        }
    }

    private synchronized void read(int i) {
        //获取命令符
        Command command = commands.get(i);
        //获取检查器
        DataCheck checker = checkers.get(i);
        //创建报警器
        DataAlarm alarm = alarms.get(i);

        //获取设备编号
        String number = command.getNumber();
        //获取参数个数
        int count = command.getCount();
        //获取设备id
        int deviceId = deviceDao.getId(port, number);

        int bit = deviceDao.getBit(deviceId);

        String id = port + "#" + number;

        //读取数据
        String data = (String) redisBase.valueOps().get("temp_" + id);

        if (data != null) {
            //截取数据
            String[] arrayData = DataFormat.subData(data, bit);

            //检查数据是否异常
            String exception = checker.check(arrayData);

            //报警处理
            //初始化报警信号
            if (!exception.equals("ER")) {
                alarmLight.put(id, true);
            } else {
                alarmLight.put(id, false);
            }
            //报警开始
            if (null == preLight.get(id) && alarmLight.get(id)) {
                preLight.put(id, alarmLight.get(id));
                int alarmId = alarm.start(arrayData, exception);
                alarmIds.put(id, alarmId);

            } else if (preLight.get(id) != alarmLight.get(id)) {
                //报警结束
                alarm.end(alarmIds.get(id));
                preLight.remove(id);
            }
//            String tableName = "data_" + IP + ":" + id;
//
//            //存储数据
//            DataEntity dataEntity = new DataEntity(0, deviceId, port, number, null, exception, count, bit, data, tableName);
//            dataDao.add(dataEntity);

            Date date = new Date();
            redisBase.hashOps().put("data_" + dateForm.format(date) + "_" + id, timeForm.format(date), timeForm.format(date) + ":" + data);
            data = null;
        }
    }
}
