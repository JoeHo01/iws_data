package com.xj.iws.http.dao;

import com.xj.iws.http.entity.DataEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by XiaoJiang01 on 2017/3/13.
 */
@Repository
public interface DataDao {
    int add(DataEntity dataEntity);
}
