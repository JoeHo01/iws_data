package com.xj.iws.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by XiaoJiang01 on 2017/6/14.
 */
public class TimeUtil {

    /**
     * 获取日期
     * @param skewing 偏移量, 0为当前日期
     * @return
     */
    public static String getDate(int skewing) {
        SimpleDateFormat dateForm = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, skewing);
        return dateForm.format(calendar.getTime());
    }
}
