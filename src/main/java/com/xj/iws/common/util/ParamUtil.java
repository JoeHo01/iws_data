package com.xj.iws.common.util;

/**
 * Created by XiaoJiang01 on 2017/5/11.
 */
public class ParamUtil {

    public static String stringify(String... arrayParam) {
        StringBuffer strParam = new StringBuffer("{");
        for (int i = 0; i < arrayParam.length; i++) {
            int separate = arrayParam[i].indexOf(":");
            String name = arrayParam[i].substring(0,separate);
            String value = arrayParam[i].substring(separate + 1,arrayParam[i].length());
            if ('[' == value.charAt(0) && ']' == value.charAt(value.length()-1)){
                strParam.append("\"" + name + "\"" + ":" + value + ",");
            }else {
                strParam.append("\"" + name + "\"" + ":" + "\"" + value + "\"" + ",");
            }
        }
        return strParam.substring(0,strParam.length()-1) + "}";
    }
}
