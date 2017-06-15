package com.xj.iws.server.receive;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/24.
 */
public class Test {
    public static void main(String[] args0){
        String code = "06030F780002";
        System.out.println(code + CRC16.checkCode(code));

    }
}
