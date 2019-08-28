package com.vahsu.kebiao.HtmlUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseDate {

    public static String[] getDates(String dateText){
        int mondayMonth = 0;
        int mondayDay = 0;
        int sundayMonth = 0;
        int sundayDay = 0;
        String regex = "[0-9]{0,2}周.*([0-9]{2})/([0-9]{0,2})-([0-9]{0,2})/([0-9]{0,2})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dateText);
        while (matcher.find()){
            mondayMonth = Integer.parseInt(matcher.group(1));
            mondayDay = Integer.parseInt(matcher.group(2));
            sundayMonth = Integer.parseInt(matcher.group(3));
            sundayDay = Integer.parseInt(matcher.group(4));
        }

        //日期格式 "MM-DD"
        //一周每天的日期
        String[] dates = new String[7];
        int i = 0;
        for (;i < 7 - sundayDay; i++){
            dates[i] = String.valueOf(mondayMonth)+"-"+String.valueOf(mondayDay +i);
        }
        for (;i < 7; i++){
            dates[i] = String.valueOf(sundayMonth)+"-"+String.valueOf(sundayDay - 6 + i);
        }
        return dates;
    }
}

