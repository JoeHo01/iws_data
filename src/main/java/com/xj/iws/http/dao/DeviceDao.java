package com.xj.iws.http.dao;

import com.xj.iws.http.entity.PointFieldEntity;
import com.xj.iws.server.receive.Command;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by XiaoJiang01 on 2017/3/14.
 */
@Repository
public interface DeviceDao {

    String getPort(String groupId);

    List<Command> getCommands(String groupId);

    Command getCommand(String deviceId);

    List<String> getAddress(int termId);

    List<PointFieldEntity> getPointField(int termId);

    int getId(@Param("port") String port,@Param("number") String number);

    List<String> getRunningGroup();

    int getCount(String termId);

    int getType(String termId);

    int getTermId(int deviceId);

    int exception(@Param("port") String port,@Param("number") String number);

    String getPortByDevice(String deviceId);

    int getBit(int deviceId);

    int getBitByType(String type);
}