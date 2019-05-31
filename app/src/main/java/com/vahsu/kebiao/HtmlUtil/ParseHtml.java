package com.vahsu.kebiao.HtmlUtil;

import com.vahsu.kebiao.DBUtil.CourseEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseHtml {
    private List<CourseEntity> courseList = new ArrayList<CourseEntity>();
    List<CourseCNTL> courseCNTLList;
    private Document doc;


    public ParseHtml(String HtmlData) {
        doc = Jsoup.parse(HtmlData);
        parseTable3();
        parseTable2();


    }

    private void parseTable2() {
        int num = 0;
        int courseLength;
        String courseName;
        String classroom;
        String type;
        String lecturer;

        String[] dates = null;

        //选中第2个table标签,其中包含课表信息
        Element table2 = doc.select("table").get(1);
        Elements trList = table2.select("tr");
        for (int i = 2; i < trList.size(); i++) {
            Elements tdList = trList.get(i).select("td");
            if (tdList.size() > 0) {
                ParseDate parseDate = new ParseDate(tdList.get(0).text());
                dates = parseDate.getDates();
            }
            int courseLengthSum = 0;
            int k = 0;
            for (int j = 1; j < tdList.size(); j++) {
                Element td = tdList.get(j);
                if (td.hasAttr("colspan")) {
                    courseLength = Integer.parseInt(td.attr("colspan"));
                } else {
                    courseLength = 1;
                }
                if ("".equals(td.ownText())) {
                    courseName = null;
                } else {
                    courseName = td.ownText();
                }
                if (td.select("font").size() > 0) {
                    classroom = td.select("font").get(0).text();
                } else {
                    classroom = null;
                }
                type = null;
                lecturer = null;
                if (null != courseName && null != classroom) {
                    for (CourseCNTL courseCNTL : courseCNTLList) {
                        if (courseCNTL.getCourseCode().equals(courseName)) {
                            courseName = courseCNTL.getCourseName();
                            type = courseCNTL.getType();
                            lecturer = courseCNTL.getLecturer();
                            break;
                        }
                    }
                }
                courseList.add(new CourseEntity(num, courseLength, courseName, classroom, type, lecturer, i - 1, k + 1, dates[k]));
                num++;
                courseLengthSum += courseLength;
                k = courseLengthSum / 12;
            }
        }
    }

    private void parseTable3() {
        courseCNTLList = new ArrayList<>();
        String courseCode;
        String courseName;
        String type;
        String lecturer;
        //选中第3个table标签
        Element table3 = doc.select("table").get(2);
        Elements tdList = table3.select("td");
        String regex1 = "\\((.*?)\\)(.*?) .*";
        String regex2 = "-(.).(.).\\[(.)] .*? 师\\[(.*?)] ";
        Pattern pattern1 = Pattern.compile(regex1);
        Pattern pattern2 = Pattern.compile(regex2);
        for (Element td : tdList) {
            String tdText = td.text();
            Matcher matcher1 = pattern1.matcher(tdText);
            if (matcher1.find()){
                courseName = matcher1.group(2);
                Matcher matcher2 = pattern2.matcher(tdText);
                while (matcher2.find()){
                    courseCode = matcher1.group(1)+matcher2.group(1)+matcher2.group(2);
                    type = matcher2.group(3);
                    lecturer = matcher2.group(4);
                    courseCNTLList.add(new CourseCNTL(courseCode, courseName, type, lecturer));
                }
            }

        }
    }

    public List<CourseEntity> getCourseList() {
        return courseList;
    }
}
