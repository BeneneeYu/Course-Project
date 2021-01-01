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
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-07-16 09:59
 **/
@WebServlet(name = "FootprintServlet", urlPatterns = "/FootprintServlet")
public class FootprintServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        ArrayList<String> imageUIDStrings = (ArrayList<String>) req.getSession().getAttribute("footprint");
        if (null != imageUIDStrings) {
            ArrayList<travelimage> travelimages = new ArrayList<travelimage>();
            ImageDao imageDao = new ImageDao();
            for (String imageUIDString : imageUIDStrings) {
                travelimages.add(imageDao.findById(Integer.parseInt(imageUIDString)));
            }
            req.setAttribute("footprint",travelimages);
        }
        req.getRequestDispatcher("footprint.jsp").forward(req,resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
