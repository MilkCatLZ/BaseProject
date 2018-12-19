package com.base.util;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Syokora on 2016/8/20.
 */
public class Phone {
    /**
     * 手机号验证
     *
     * @param str
     *
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 判断是否为固定电话号码
     *
     * @param number 固定电话号码
     *
     * @return
     */
    public static boolean isFixedPhone(String number) {
        //用于匹配固定电话号码
        if(number.indexOf("0")!=0)return false;//第一位是0
        String REGEX_FIXEDPHONE = "^(010|02\\d|0[3-9]\\d{2})?\\d{10,12}$";
        Pattern PATTERN_FIXEDPHONE = Pattern.compile(REGEX_FIXEDPHONE);
        Matcher match = PATTERN_FIXEDPHONE.matcher(number);
        if (match.matches()) {
            if (number.indexOf("010") == 0 || number.indexOf("02") == 0) {
                return number.length() >= 10 && number.length() <= 11;
            } else {
                return number.length() >= 11 && number.length() <= 12;
            }
        } else {
            return false;
        }
    }

    /**
     * 判断是否是电话号码，可判断手机或固话
     *
     * @param number
     *
     * @return
     */
    public static boolean isPhone(String number) {
        return isMobile(number) || isFixedPhone(number);
    }

    /**
     * 电话号码1300000000->130****0000
     *
     * @param phone
     *
     * @return
     */
    public static String phoneEncode(@NonNull String phone) {
        if (StringUtils.isEmpty(phone)) {
            return "";
        }
        String[] phones = phone.trim()
                               .split("");
        StringBuilder stringBuilder = new StringBuilder();
        //split出来phones[0]为空字符串，所以从1开始
        for(int i = 1; i < phones.length; i++) {
            if (i >= 4 && i <= 7) {
                stringBuilder.append("*");
            } else {
                stringBuilder.append(phones[i]);
            }
        }
        return stringBuilder.toString();

    }

    /**
     * 拨打电话
     *
     * @param context
     * @param phone
     */
    public static void call(Context context, String phone) {
        if (StringUtils.isNotEmpty(phone)) {
            phone = phone.replace("-", "");
            Uri telUri = Uri.parse("tel:" + phone);
            Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
