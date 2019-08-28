package com.vahsu.kebiao.HtmlUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseDefaultHtml {
    public static String getScheduleAddress(String defaultHtml){
        Document doc = Jsoup.parse(defaultHtml);
        Elements a = doc.getElementsContainingOwnText("查看课表");
        String href = a.attr("onclick");
        String regex = "[\\S\\s]*\\(\"([\\S\\s]*?)\"[\\S\\s]*\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(href);
        String scheduleAddress = null;
        if (matcher.find()) {
            scheduleAddress = matcher.group(1);
        }
        return scheduleAddress;

    }
}
