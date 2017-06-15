package com.xj.iws.server.data;

/**
 * Created by XiaoJiang01 on 2017/3/16.
 */
public class DataFormat {

    /**
     * 预处理数据
     * @param data
     * @param start
     * @param end
     * @return
     */
    public static String preData(String data, int start, int end) {
        String result = data.replace(" ", "");
        result = result.substring(start, result.length() - end);
        return result;
    }

    /**
     * 截取数据
     *
     * @param data
     * @param step
     * @return
     */
    public static String[] subData(String data, int step) {
        int count = data.length()/step;
        String[] result = new String[count];
        for (int i = 0, j = 0; j < count; i += step, j++) {
            result[j] = data.substring(i, i + step);
        }
        return result;
    }
}
