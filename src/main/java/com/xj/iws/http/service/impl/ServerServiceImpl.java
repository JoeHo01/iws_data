package com.xj.iws.http.service.impl;

import com.xj.iws.common.enums.ErrorCodeEnum;
import com.xj.iws.common.util.DataWrapper;
import com.xj.iws.http.dao.ServerDao;
import com.xj.iws.http.service.ServerService;
import com.xj.iws.http.dao.redis.RedisBase;
import com.xj.iws.server.receive.SerialServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by XiaoJiang01 on 2017/4/28.
 */
@Service
public class ServerServiceImpl implements ServerService {
    @Autowired
    ServerDao serverDao;
    @Autowired
    SerialServer serialServer;

    @Override
    public DataWrapper<Void> startServer() {
        DataWrapper<Void> dataWrapper = new DataWrapper<>();
        String ip = serverDao.getIP();
        String port = serverDao.getPort();
        serialServer.setParam(ip,Integer.parseInt(port));
        serialServer.init();
        return dataWrapper;
    }

    @Override
    public DataWrapper<Void> closeServer() {
        DataWrapper<Void> dataWrapper = new DataWrapper<>();
        serialServer.close();
        return dataWrapper;
    }
}
