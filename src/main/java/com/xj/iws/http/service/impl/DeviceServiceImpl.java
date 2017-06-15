package com.xj.iws.http.service.impl;

import com.xj.iws.common.enums.ErrorCodeEnum;
import com.xj.iws.http.dao.DeviceDao;
import com.xj.iws.http.dao.ServerDao;
import com.xj.iws.server.data.DataReader;
import com.xj.iws.http.service.DeviceService;
import com.xj.iws.common.util.DataWrapper;
import com.xj.iws.server.receive.Command;
import com.xj.iws.http.dao.redis.RedisBase;
import com.xj.iws.server.receive.SerialServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by XiaoJiang01 on 2017/3/14.
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    DeviceDao deviceDao;
    @Autowired
    DataReader reader;
    @Autowired
    ServerDao serverDao;
    @Autowired
    SerialServer serialServer;
    @Autowired
    RedisBase redisBase;


    @Override
    public DataWrapper<Void> start(List<String> groupIds) {
        DataWrapper<Void> dataWrapper = new DataWrapper<>();
        try {
            String port = deviceDao.getPort(groupIds.get(0));
            List<Command> commands = deviceDao.getCommands(groupIds.get(0));
            read(port, commands);
        } catch (Exception e) {

        }
        return dataWrapper;
    }

    @Override
    public DataWrapper<Void> close(List<String> groupIds) {
        DataWrapper<Void> dataWrapper = new DataWrapper<>();
        String port = deviceDao.getPort(groupIds.get(0));
        reader.close(port);
        serialServer.closePort(port);
        SerialServer.removeReader(port);
        SerialServer.removeCommand(port);
        return dataWrapper;
    }

    @Override
    public DataWrapper<Void> startAll() {
        DataWrapper<Void> dataWrapper = new DataWrapper<>();
        List<String> groupIds = deviceDao.getRunningGroup();
        for (String groupId : groupIds) {
            String port = deviceDao.getPort(groupId);
            List<Command> commands = deviceDao.getCommands(groupId);
            read(port, commands);
        }

        return dataWrapper;
    }

    @Override
    public DataWrapper<String> test(String port, List<Map<String, String>> devices) {
        DataWrapper<String> dataWrapper = new DataWrapper<>();
        List<Command> commands = new ArrayList<>();
        for (Map<String, String> device : devices) {
            Command command = new Command();
            command.setCount(deviceDao.getCount(device.get("termId")));
            command.setNumber(device.get("number"));
            command.setType(deviceDao.getType(device.get("termId")));
            if (command.getType() == 2) {
                command.setAddress(deviceDao.getAddress(Integer.parseInt(device.get("termId"))));
                command.addressCode();
            } else {
                command.autoCode();
            }
            commands.add(command);
        }

        StringBuffer result = new StringBuffer();
        for (Command command : commands) {
            String number = command.getNumber();
            if (serialServer.manual(port, command) == null) {
                result.append(number + ",");
            }
        }
        String data = result.toString();
        if (data.length() > 0) data = data.substring(0, data.length() - 1);
        dataWrapper.setData(data);
        return dataWrapper;
    }

    @Override
    public DataWrapper<String> turnPump(String deviceId, String fieldNum, String pumpStatus) {
        DataWrapper<String> dataWrapper = new DataWrapper<>();
        String port = deviceDao.getPortByDevice(deviceId);
        Command command = deviceDao.getCommand(deviceId);
        command.manualCode(Integer.parseInt(fieldNum), Integer.parseInt(pumpStatus));
        String result = serialServer.manual(port, command);

        if (result == null || !command.getCode().equals(result)) {
            dataWrapper.setErrorCode(ErrorCodeEnum.Error);
        }
        return dataWrapper;
    }

    @Override
    public DataWrapper<String> checkTable(String type, String count, List<String> address, String port, String number) {
        DataWrapper<String> dataWrapper = new DataWrapper<>();

        Command command = new Command();
        command.setType(Integer.parseInt(type));
        command.setCount(Integer.parseInt(count));
        command.setNumber(number);

        switch (Integer.parseInt(type)) {
            case 1:
                command.autoCode();
                break;
            case 2:
                command.setAddress(address);
                command.addressCode();
                break;
            default:
                command.autoCode();
                break;
        }

        String result = serialServer.manual(port, command);
        int bit = deviceDao.getBitByType(type);
        int length = bit * Integer.parseInt(count);

        if (result != null && result.length() == length) {
            dataWrapper.setData("点表验证通过!");
        } else {
            dataWrapper.setData("点表与设备不匹配,请检查点表字段或切换设备检测");
        }
        return dataWrapper;
    }

    private void read(String port, List<Command> commands) {
        Date date = new Date();
        SimpleDateFormat dateForm = new SimpleDateFormat("yyyyMMdd");
        for (Command command : commands) {
            switch (command.getType()) {
                case 1:
                    command.autoCode();
                    break;
                case 2:
                    command.setAddress(deviceDao.getAddress(command.getTermId()));
                    command.addressCode();
                    break;
                default:
                    command.autoCode();
                    break;
            }
            redisBase.setOps().add("keys_" + dateForm.format(date), dateForm.format(date) + "_" + port + "#" + command.getNumber());
        }

        String IP = serverDao.getIP();

        SerialServer.setCommand(port, commands);
        serialServer.startPort(port);

        DataReader reader = this.reader.create(IP, port, commands);
        reader.read();
    }
}
