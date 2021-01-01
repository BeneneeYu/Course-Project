package com.photoSharing.servlet;

import com.photoSharing.dao.ImageDao;
import com.photoSharing.entity.travelimage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @program: Project
 * @description: 展示首页，初始化三个最新三个最热
 * @author: Shen Zhengyu
 * @create: 2020-07-15 22:58
 **/
@WebServlet(name = "index", urlPatterns = "/index")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ImageDao imageDao = new ImageDao();
        req.setCharacterEncoding("utf-8");

        ArrayList<travelimage> hottestPhotos = new ArrayList<>(imageDao.findHottestPhotos());
        ArrayList<travelimage> latestPhotos = new ArrayList<>(imageDao.findLatestPhotos());
        req.setAttribute("hottestPhotos",hottestPhotos);
        req.setAttribute("latestPhotos",latestPhotos);
        req.getRequestDispatcher("/index.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
