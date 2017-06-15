package com.xj.iws.http.controller;

import com.xj.iws.http.service.DeviceService;
import com.xj.iws.common.util.DataWrapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by XiaoJiang01 on 2017/3/14.
 */
@Controller
@RequestMapping("api/device")
public class DeviceController {
    @Autowired
    DeviceService deviceService;

    @RequestMapping(value = "startDevice", method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> start(
            @RequestParam String param
    ) {
        Map<String,String> paramMap = (Map<String,String>)JSONObject.fromObject(param);
        String groupId = paramMap.get("groupId");
        List<String> groupIds = new ArrayList<>();
        groupIds.add(groupId);
        DataWrapper<Void> dataWrapper = deviceService.start(groupIds);
        return dataWrapper;
    }

    @RequestMapping(value = "closeDevice", method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> close(
            @RequestParam String param
    ) {
        Map<String,String> paramMap = (Map<String,String>)JSONObject.fromObject(param);
        String groupId = paramMap.get("groupId");
        List<String> groupIds = new ArrayList<>();
        groupIds.add(groupId);
        return deviceService.close(groupIds);
    }

    @RequestMapping(value = "startAll", method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> startAll() {
        return deviceService.startAll();
    }

    @RequestMapping(value = "testDevice", method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<String> test(
            @RequestParam String param
    ) {
        Map<String,Object> paramMap = (Map<String,Object>)JSONObject.fromObject(param);
        String port = (String) paramMap.get("port");
        List<Map<String,String>> devices = (List<Map<String,String>>) paramMap.get("devices");
        return deviceService.test(port,devices);
    }

    @RequestMapping(value = "turnPump", method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<String> turnPump(
            @RequestParam String param
    ) {
        Map<String,Object> paramMap = (Map<String,Object>)JSONObject.fromObject(param);
        String deviceId = (String) paramMap.get("deviceId");
        String fieldNum = (String) paramMap.get("fieldNum");
        String pumpStatus = (String) paramMap.get("pumpStatus");
        return deviceService.turnPump(deviceId,fieldNum,pumpStatus);
    }

    @RequestMapping(value = "checkTable", method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<String> checkTable(
            @RequestParam String param
    ) {
        Map<String,Object> paramMap = (Map<String,Object>)JSONObject.fromObject(param);
        String type = (String) paramMap.get("type");
        String count = (String) paramMap.get("count");
        List<String> address = (List<String>) paramMap.get("address");
        String port = (String) paramMap.get("port");
        String number = (String) paramMap.get("number");
        return deviceService.checkTable(type,count,address,port,number);
    }
}
