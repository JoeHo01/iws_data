package com.xj.iws.http.controller;

import com.xj.iws.common.util.DataWrapper;
import com.xj.iws.http.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by XiaoJiang01 on 2017/4/28.
 */
@Controller
@RequestMapping("api/data")
public class DataController {
    @Autowired
    DataService dataService;

    @RequestMapping(value = "saveData",method = RequestMethod.POST)
    @ResponseBody
    public DataWrapper<Void> saveData(){
        return dataService.saveAll();
    }
}
