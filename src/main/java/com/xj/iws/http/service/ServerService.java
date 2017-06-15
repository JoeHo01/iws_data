package com.xj.iws.http.service;

import com.xj.iws.common.util.DataWrapper;

/**
 * Created by XiaoJiang01 on 2017/4/28.
 */
public interface ServerService {
    DataWrapper<Void> startServer();

    DataWrapper<Void> closeServer();
}
