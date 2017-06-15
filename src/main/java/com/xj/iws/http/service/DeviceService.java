package com.xj.iws.http.service;

import com.xj.iws.common.util.DataWrapper;

import java.util.List;
import java.util.Map;

/**
 * Created by XiaoJiang01 on 2017/3/14.
 */
public interface DeviceService {
    DataWrapper<Void> start(List<String> groupIds);

    DataWrapper<Void> close(List<String> groupIds);

    DataWrapper<Void> startAll();

    DataWrapper<String> test(String port, List<Map<String, String>> devices);

    DataWrapper<String> turnPump(String deviceId, String fieldNum, String pumpStatus);

    DataWrapper<String> checkTable(String type, String count, List<String> address, String port, String number);
}
