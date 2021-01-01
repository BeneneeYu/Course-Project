package com.photoSharing.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @program: Project
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-07-16 20:09
 **/
@WebServlet(name = "PhotoServlet", urlPatterns = "/PhotoServlet")

public class PhotoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletOutputStream outStream = resp.getOutputStream();// 得到向客户端输出二进制数据的对象
        String path = req.getParameter("Path");
        FileInputStream fis = new FileInputStream("/usr/local/SZYTomcat/tomcat/webapps/resources/travel-images/medium/" + path ); // 以byte流的方式打开文件
        // 读数据
        byte data[] = new byte[1000];
        while (fis.read(data) > 0)
        {
            outStream.write(data);
        }
        fis.close();
        resp.setContentType("image/*"); // 设置返回的文件类型
        outStream.write(data); // 输出数据

        outStream.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
