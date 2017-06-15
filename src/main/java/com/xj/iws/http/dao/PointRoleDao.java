package com.xj.iws.http.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by XiaoJiang01 on 2017/4/21.
 */
@Repository
public interface PointRoleDao {
    List<Map<String,String>> getStatus(int i);
}
