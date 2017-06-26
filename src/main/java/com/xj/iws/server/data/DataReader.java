package com.xj.iws.server.data;

import com.xj.iws.http.dao.AlarmDao;
import com.xj.iws.http.dao.DataDao;
import com.xj.iws.http.dao.DeviceDao;
import com.xj.iws.server.receive.Command;
import com.xj.iws.http.dao.redis.RedisBase;
import com.xj.iws.server.receive.SerialServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.List;

/**
 * Created by XiaoJiang01 on 2017/4/13.
 */
@Component
public class DataReader {
    @Autowired
    RedisBase redisBase;
    @Autowired
    DataDao dataDao;
    @Autowired
    DeviceDao deviceDao;
    @Autowired
    AlarmDao alarmDao;
    @Autowired
    DataCheck checker;
    @Autowired
    DataAlarm alarm;

    String IP;
    String port;
    List<Command> commands;

    List<DataCheck> checkers;
    List<DataAlarm> alarms;

    public DataReader() {

    }

    public DataReader(RedisBase redisBase,DataDao dataDao,DeviceDao deviceDao,DataCheck checker,DataAlarm alarm, String IP, String port, List<Command> commands) {
        this.redisBase = redisBase;
        this.dataDao = dataDao;
        this.deviceDao = deviceDao;
        this.IP = IP;
        this.port = port;
        this.commands = commands;
        this.checker = checker;
        this.alarm = alarm;
        this.checkers = check();
        this.alarms = alarm();
    }

    public DataReader create(String IP, String port, List<Command> commands) {
        return new DataReader(redisBase,dataDao,deviceDao,checker,alarm,IP,port,commands);
    }

    public void read() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new DataTask(redisBase,dataDao,deviceDao,IP,port,commands,checkers,alarms), 0, 1 * 1000);
        SerialServer.setReader(port, timer);
    }

    public void close(String port) {
        SerialServer.getReader().get(port).cancel();
    }

    /**
     * 创建检查器
     *
     * @return
     */
    private List<DataCheck> check() {
        List<DataCheck> checkers = new ArrayList<>();
        for (Command command : commands) {
            DataCheck checker = this.checker.create(port, command.getNumber());
            checkers.add(checker);
        }
        return checkers;
    }

    /**
     * 创建报警器
     *
     * @return
     */
    private List<DataAlarm> alarm() {
        List<DataAlarm> alarms = new ArrayList<>();
        for (Command command : commands) {
            DataAlarm alarm = this.alarm.create(port, command.getNumber());
            alarms.add(alarm);
        }
        return alarms;
    }
}
