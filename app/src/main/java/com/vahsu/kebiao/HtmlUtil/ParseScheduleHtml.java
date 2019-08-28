package com.vahsu.kebiao.HtmlUtil;

import com.vahsu.kebiao.DBUtil.CourseEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseScheduleHtml {
    private List<CourseEntity> courseList = new ArrayList<CourseEntity>();
    private Map<String, CourseNTL> courseNTLMap = new HashMap<>();
    private Document doc;
    private String firstYear;
    private String lastYear;
    private String period;


    public ParseScheduleHtml(String HtmlData) {
        doc = Jsoup.parse(HtmlData);
        parseTable3();
        parseTable2();
        parseTable1();
    }

    //解析第1个table标签(class名tab3),其中有年份信息
    private void parseTable1() {
        Element table1 = doc.selectFirst("table.tab3");
        Element title_nob = table1.getElementsByClass("title nob").first();
        String regex = ".*\\((\\d*)-(\\d*)学年第(.)学期\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(title_nob.text());
        if (matcher.find()) {
            firstYear = matcher.group(1);
            lastYear = matcher.group(2);
            period = matcher.group(3);
        }
    }

    //解析第2个table标签(class名tab1),其中包含课表信息
    private void parseTable2() {
        int num = 0;

        String[] dates = null;
        //选中第2个table标签(class名tab1)
        Element table2 = doc.selectFirst("table.tab1");
        Elements trList = table2.select("tr");
        //从第3个tr标签开始循环
        for (int i = 2; i < trList.size(); i++) {
            Elements tdList = trList.get(i).select("td");
            if (tdList.size() > 0) {
                //第1个td标签包括日期信息
                dates = ParseDate.getDates(tdList.get(0).text());
            }
            int courseLengthSum = 0;
            int k = 0;
            //从第2个td标签开始循环,包括课程信息
            for (int j = 1; j < tdList.size(); j++) {
                Element td = tdList.get(j);
                int courseLength = 1;
                String courseName = td.ownText();
                String classroom = null;
                String type = null;
                String lecturer = null;
                if (td.hasAttr("colspan")) {
                    courseLength = Integer.parseInt(td.attr("colspan"));
                }
                if ("".equals(courseName)) {
                    courseName = null;
                }
                Element font = td.selectFirst("font");
                if (font  != null) {
                    classroom = font.text();
                }
                CourseNTL courseNTL = courseNTLMap.get(courseName);
                if (courseNTL != null) {
                    courseName = courseNTL.getCourseName();
                    type = courseNTL.getType();
                    lecturer = courseNTL.getLecturer();

                }
                courseList.add(new CourseEntity(num, courseLength, courseName, classroom, type, lecturer, i - 1, k + 1, dates[k]));
                num++;
                courseLengthSum += courseLength;
                k = courseLengthSum / 12;
            }
        }
    }

    //解析第3个table标签，课程代码与名称的对应关系
    private void parseTable3() {
        String courseCode;
        String courseName;
        String type;
        String lecturer;
        //选中第3个table标签
        Element table3 = doc.selectFirst("table.tab2");
        Elements tdList = table3.select("td");
        String regex1 = "\\((.*?)\\)(.*?) .*";
        String regex2 = "-(.).(.).\\[(.)] .*? 师\\[(.*?)] ";
        Pattern pattern1 = Pattern.compile(regex1);
        Pattern pattern2 = Pattern.compile(regex2);
        for (Element td : tdList) {
            String tdText = td.text();
            Matcher matcher1 = pattern1.matcher(tdText);
            if (matcher1.find()) {
                courseName = matcher1.group(2);
                Matcher matcher2 = pattern2.matcher(tdText);
                while (matcher2.find()) {
                    courseCode = matcher1.group(1) + matcher2.group(1) + matcher2.group(2);
                    type = matcher2.group(3);
                    lecturer = matcher2.group(4);
                    courseNTLMap.put(courseCode, new CourseNTL(courseName, type, lecturer));
                }
            }
        }
    }

    public List<CourseEntity> getCourseList() {
        return courseList;
    }

    public String getFirstYear() {
        return firstYear;
    }

    public String getLastYear() {
        return lastYear;
    }

    public String getPeriod() {
        return period;
    }
}
