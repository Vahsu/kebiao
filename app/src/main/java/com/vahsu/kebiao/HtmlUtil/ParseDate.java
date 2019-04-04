package com.vahsu.kebiao.HtmlUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseDate {
    private String dateString;
    private int mondayMonth;
    private int mondayDay;
    private int sundayMonth;
    private int sundayDay;
    //一周每天的日期
    private String[] dates;

    ParseDate(String dateText){
        this.dateString = dateText;
        parseDate();
    }
    private void parseDate(){
        String regex = "[0-9]{0,2}周.*([0-9]{2})/([0-9]{0,2})-([0-9]{0,2})/([0-9]{0,2})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dateString);
        while (matcher.find()){
            mondayMonth = Integer.parseInt(matcher.group(1));
            mondayDay = Integer.parseInt(matcher.group(2));
            sundayMonth = Integer.parseInt(matcher.group(3));
            sundayDay = Integer.parseInt(matcher.group(4));
        }

        //日期格式 "MM-DD"
        dates = new String[7];
        int i = 0;
        for (;i < 7 - sundayDay; i++){
            dates[i] = String.valueOf(mondayMonth)+"-"+String.valueOf(mondayDay +i);
        }
        for (;i < 7; i++){
            dates[i] = String.valueOf(sundayMonth)+"-"+String.valueOf(sundayDay - 6 + i);
        }
    }

    public String[] getDates() {
        return dates;
    }

}

