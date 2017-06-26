package com.xj.iws.http.controller;

import com.xj.iws.common.util.DataWrapper;
import com.xj.iws.http.dao.redis.RedisBase;
import com.xj.iws.http.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

/**
 * Created by XiaoJiang01 on 2017/4/28.
 */
@Controller
@RequestMapping("api/test")
public class TestController {
    @Autowired
    RedisBase redisBase;

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> remove() {
        Set<String> keys_device_running = redisBase.setOps().members("keys_device_running");
        for (String key : keys_device_running) {
            if (key.indexOf("172.16.2.220") != -1){
                redisBase.setOps().remove("keys_device_running", key);
            }
        }

        return new DataWrapper<>();
    }


}
