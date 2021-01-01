package com.photoSharing.utils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * @program: Project
 * @description: 跳转工具类
 * @author: Shen Zhengyu
 * @create: 2020-07-13 13:58
 **/
public class WebUtils {
    public static void goTo(HttpServletRequest request,
                            HttpServletResponse response, Object uri) throws ServletException,
            IOException {
        if (uri instanceof RequestDispatcher) {
            ((RequestDispatcher) uri).forward(request, response);
        } else if (uri instanceof String) {
            response.sendRedirect(request.getContextPath() + uri);
        }
    }

    /**
    * @Description: 得到北京时间
    * @Param: []
    * @return: java.util.Date
    * @Author: Shen Zhengyu
    * @Date: 2020/7/19
    */
    public Date getBJTime() {
        Date date = new Date();
        SimpleDateFormat bjSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     // 北京
        try {
            return bjSdf.parse(bjSdf.format(date));

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String tenDigitPathGetter(){
        Random random = new Random();
        String result="";
        for (int j=0;j<10;j++)
        {
            result+=random.nextInt(10);
        }
        return result;
    }

    
}
