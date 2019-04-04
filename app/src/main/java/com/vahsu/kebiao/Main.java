package com.vahsu.kebiao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] arg){
        String text = "(射1)射频通信电路 (ID[L1591]学分[3.5] 105147-L01([理] 时[48] 师[郭亚莎] 室[6A105,6A307,6A207])2016130701-2105147-R02([实] 时[8] 师[郭亚莎2] 室[6B603])2016130702";
        String regex2 = "\\((.*?)\\)(.*?) .*";
        Pattern pattern = Pattern.compile(regex2);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()){
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));

        }
    }
}
