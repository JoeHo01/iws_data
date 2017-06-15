package com.xj.iws.common.util;

/**
 * Created by XiaoJiang01 on 2017/6/12.
 */
public class SortUtil {

    public static void sort(int[] arrays) {
        int length = arrays.length;
                 if (null == arrays || length < 1) {
                         System.out.println("input error!");
                         return;
                     }
                 quickSort(arrays, 0, length - 1);
             }

    private static int partition(int []array,int lo,int hi){
        //固定的切分方式
        int key=array[lo];
        while(lo<hi){
            while(array[hi]>=key&&hi>lo){//从后半部分向前扫描
                hi--;
            }
            array[lo]=array[hi];
            while(array[lo]<=key&&hi>lo){//从前半部分向后扫描
                lo++;
            }
            array[hi]=array[lo];
        }
        array[hi]=key;
        return hi;
    }

    private static void quickSort(int[] array,int lo ,int hi){
        if(lo>=hi) return;

        int index=partition(array,lo,hi);
        quickSort(array,lo,index-1);
        quickSort(array,index+1,hi);
    }

    public static int min(int[] array){
        int min = array[0];
        for (int i = 1; i <array.length ; i++) {
            if (min > array[i]) min = array[i];
        }
        return min;
    }

    public static int max(int[] array){
        int max = array[0];
        for (int i = 1; i <array.length ; i++) {
            if (max < array[i]) max = array[i];
        }
        return max;
    }

}
