package com.base.util;


/**
 * Created by LZ on 2017/3/9.
 */

public class NumberUtil {
    
    /**
     *
     * @param src
     * @param max
     * @return
     */
    public static String calString(String src,int max){
        if(StringUtils.isNotEmpty(src)){
            StringBuilder stringBuilder=new StringBuilder("");
            if(src.contains(".")){
                String[] cache=src.split("\\.");
                String[] cache1=cache[1].split("");
                String[] temp=new String[max];
                for(int i=0;i<max;i++){
                    temp[i]=cache1[i+1];
                }
                stringBuilder.append(cache[0]);
                stringBuilder.append('.');
                for(String s : temp) {
                    stringBuilder.append(s);
                }
              
                return stringBuilder.toString();
            }
        }
        return src;
    }
}
