package com.xj.iws.http.dao;

import com.xj.iws.http.entity.DataEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by XiaoJiang01 on 2017/3/13.
 */
@Repository
public interface DataDao {
    void addAll(@Param("tableName") String tableName,@Param("datas") List<DataEntity> datas);
}
