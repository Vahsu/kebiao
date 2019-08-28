package com.vahsu.kebiao.HtmlUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;

public class GetHtml {
    private String cookieVal;
    private StringBuilder rs;
    private String username;
    private String password;
    private StringBuilder courseHtml;

    public GetHtml(String username, String password){
        this.username = username;
        this.password = password;
        getCookies();
        if(loginIsSuccessful()){
            downloadScheduleHtml();
        }
    }

    public String loginState(){
        if(null != rs){
            return rs.toString();
        } else {
            return null;
        }

    }
    public boolean loginIsSuccessful(){
        return "0".equals(loginState());
    }

    public String getCourseHtml(){
        if(null != courseHtml) {
            return courseHtml.toString();
        } else {
            return null;
        }
    }

    //验证账号密码并取得cookie
    private void getCookies(){
        String surl = "http://202.115.133.173:805/Common/Handler/UserLogin.ashx";
        try {
            URL url = new URL(surl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(4000);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);

            Date d = new Date();
            String sign = String.valueOf(d.getTime());
            String Md5Password1 = getMD5String(password);
            String Md5Password2 = getMD5String(username + sign + Md5Password1);
            String post = "Action=Login&userName=" + username + "&pwd=" + Md5Password2 + "&sign=" + sign;
            //执行post
            out.write(post);
            //刷新缓冲区
            out.flush();
            //关闭输出流
            out.close();
            //取得cookie
            cookieVal = connection.getHeaderField("Set-Cookie");
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(connection.getInputStream()));
            rs = new StringBuilder();
            String line;
            if((line = bufferedReader.readLine()) != null){
                rs.append(line);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getScheduleAddress(){
        String surl = "http://202.115.133.173:805/Default.aspx";
        String scheduleAddress = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(surl).openConnection();
            if (cookieVal != null){
                connection.setRequestProperty("Cookie", cookieVal);
            }
            connection.setConnectTimeout(4000);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line).append("\n");
            }
            scheduleAddress = ParseDefaultHtml.getScheduleAddress(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scheduleAddress;
    }
    private void downloadScheduleHtml(){
        try {

            String s = "http://202.115.133.173:805/" + getScheduleAddress();
            URL url = new URL(s);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (cookieVal != null) {

                connection.setRequestProperty("Cookie", cookieVal);
            }
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            courseHtml = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                courseHtml.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String getMD5String(String str) {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
           e.printStackTrace();
           return null;
        }
    }
}
