package com.xj.iws.http.dao;

import com.xj.iws.http.entity.AlarmEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by XiaoJiang01 on 2017/4/21.
 */
@Repository
public interface AlarmDao {
    AlarmEntity getAddress(int deviceId);

    int add(AlarmEntity alarmEntity);

    void updateEndTime(int id);
}
