package com.xj.iws.http.service.impl;

import com.xj.iws.common.util.DataWrapper;
import com.xj.iws.common.util.TimeUtil;
import com.xj.iws.http.service.DataService;
import com.xj.iws.http.dao.redis.RedisBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/6/14.
 */
@Service
public class DataServiceImpl implements DataService {
    @Autowired
    RedisBase redisBase;

    @Override
    public DataWrapper<Void> saveData() {
        DataWrapper<Void> dataWrapper = new DataWrapper<>();
        Set<String> keys = redisBase.setOps().members("keys_"+ TimeUtil.getDate(-1));
        for (String key : keys){
            Map<String,String> datas = redisBase.hashOps().entries(key);
        }
        return null;
    }
}
