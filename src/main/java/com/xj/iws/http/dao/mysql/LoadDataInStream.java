package com.xj.iws.http.dao.mysql;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2017/6/19.
 */
@Repository
public class LoadDataInStream {
    @Autowired
    private DataSource dataSource;

    private Connection conn;
//    private static final Logger logger = Logger.getLogger(LoadDataInStream.class);

    public int write(String table, List<String[]> fields, String fieldName) {
        String sql = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE `" + table + "` (" + fieldName + ")";
        InputStream dataStream = getTestDataInputStream(fields);
        int rows = 0;
        try {
//            long beginTime=System.currentTimeMillis();
            rows = loadFromInputStream(sql, dataStream);
//            long endTime=System.currentTimeMillis();
//            logger.info("importing "+rows+" rows data into mysql and cost "+(endTime-beginTime)+" ms!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    private InputStream getTestDataInputStream(List<String[]> fields) {
        StringBuilder builder = new StringBuilder();

        for (String[] field : fields) {
            for (int i = 0; i < field.length; i++) {
                builder.append(field[i]);
                if (i < field.length - 1) {
                    builder.append("\t");
                } else {
                    builder.append("\n");
                }
            }
        }

        byte[] bytes = builder.toString().getBytes();
        return new ByteArrayInputStream(bytes);
    }

    /**
     * load bulk data from InputStream to MySQL
     */
    private int loadFromInputStream(String loadDataSql, InputStream dataStream) throws SQLException {
        if (dataStream == null) {
//            logger.info("InputStream is null ,No data is imported");
            return 0;
        }
        conn = dataSource.getConnection();
        PreparedStatement statement = conn.prepareStatement(loadDataSql);

        int result = 0;

        if (statement.isWrapperFor(com.mysql.jdbc.Statement.class)) {
            com.mysql.jdbc.PreparedStatement mysqlStatement = statement.unwrap(com.mysql.jdbc.PreparedStatement.class);
            mysqlStatement.setLocalInfileInputStream(dataStream);
            result = mysqlStatement.executeUpdate();
        }
        return result;
    }
}
