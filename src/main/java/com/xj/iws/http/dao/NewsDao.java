package com.xj.iws.http.dao;

import com.xj.iws.http.entity.NewsEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by XiaoJiang01 on 2017/4/24.
 */
@Repository
public interface NewsDao {
    int add(NewsEntity news);
}