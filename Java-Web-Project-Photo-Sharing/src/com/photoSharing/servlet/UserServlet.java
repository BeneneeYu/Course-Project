package com.photoSharing.servlet;

import com.photoSharing.dao.ImageDao;
import com.photoSharing.dao.UserDao;
import com.photoSharing.entity.travelimage;
import com.photoSharing.entity.traveluser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: Project
 * @description: 用户相关功能
 * @author: Shen Zhengyu
 * @create: 2020-07-13 22:41
 **/
@WebServlet(name = "user", urlPatterns = "/user")
public class UserServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //响应查询我的图片方法
        String method = req.getParameter("method");
        req.setCharacterEncoding("utf-8");
        traveluser tu = (traveluser) req.getSession().getAttribute("traveluser");
        if (null == tu){
            resp.sendRedirect("index");
        }else {
            //响应查询
            if ("retrieve".equals(method)) {
                ImageDao imageDao = new ImageDao();
                List<travelimage> travelimages = imageDao.findAllByUID(tu.getUID());
                if (null != travelimages){
                    req.setAttribute("travelimages",travelimages);
                    req.getRequestDispatcher("myPhoto.jsp").forward(req,resp);
                }
                //响应更改
            }else if ("updateState".equals(method)){
                UserDao userDao = new UserDao();
                userDao.updateState((traveluser) req.getSession().getAttribute("traveluser"));
                resp.sendRedirect("FavorServlet?method=retrieve");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

