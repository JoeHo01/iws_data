package com.xj.iws.http.dao;

import org.springframework.stereotype.Repository;

/**
 * Created by XiaoJiang01 on 2017/4/28.
 */
@Repository
public interface ServerDao {
    String getPort();

    String getIP();
}
