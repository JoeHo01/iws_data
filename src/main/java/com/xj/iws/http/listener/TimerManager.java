package com.xj.iws.http.listener;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/6/20.
 */
public class TimerManager {
    //时间间隔
    private long period;
    private Date date;

    public void schedule(TimerTask task){

        Timer timer = new Timer();

        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        timer.schedule(task, date, period);
    }


    public TimerManager(int hour, int minute, int second,long period) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        //第一次执行定时任务的时间
        date = calendar.getTime();

        //如果第一次执行定时任务的时间 小于 当前的时间
        //此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。循环执行的周期则以当前时间为准
        if (date.before(new Date())) {
            date = this.addDay(date, 1);
        }

        this.period = period;
    }

    // 增加或减少天数
    private Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }
}
