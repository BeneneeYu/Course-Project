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
 * @description: 响应对图片的搜索
 * @author: Shen Zhengyu
 * @create: 2020-07-16 12:43
 **/
@WebServlet(name = "SearchPhotoServlet", urlPatterns = "/SearchPhotoServlet")
public class SearchPhotoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        String keyword = req.getParameter("keyword");
        String choose = req.getParameter("choose");
        String order = req.getParameter("order");
        ImageDao imageDao = new ImageDao();
        ArrayList<travelimage> travelimages = new ArrayList<>();
        if (null != imageDao.findAccordingToRules(choose,order,keyword)) {
            travelimages = new ArrayList<>(imageDao.findAccordingToRules(choose, order, keyword));
        }
        req.setAttribute("photos",travelimages);
        req.getRequestDispatcher("searchPhoto.jsp").forward(req,resp);
    }
}
