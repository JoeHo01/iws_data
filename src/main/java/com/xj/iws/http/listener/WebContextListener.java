package com.xj.iws.http.listener;

import com.xj.iws.common.communication.ServerRequest;
import javafx.scene.chart.XYChart;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by XiaoJiang01 on 2017/3/13.
 */

public class WebContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        new Timer().schedule(new start(), 5000);
        new TimerManager(0, 0, 10, 24 * 3600 * 1000).schedule(new saveData());

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private class start extends TimerTask {
        @Override
        public void run() {
            ServerRequest.send("http://localhost:8180/iws_data/api/server/startServer", null);
            ServerRequest.send("http://localhost:8180/iws_data/api/device/startAll", null);
        }
    }

    private class saveData extends TimerTask {
        @Override
        public void run() {
            ServerRequest.send("http://localhost:8180/iws_data/api/data/saveData", null);
        }
    }

}
