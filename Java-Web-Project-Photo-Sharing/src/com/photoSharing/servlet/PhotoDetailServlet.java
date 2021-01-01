package com.photoSharing.servlet;

import com.photoSharing.dao.ImageDao;
import com.photoSharing.dao.ImageFavorDao;
import com.photoSharing.dao.UserDao;
import com.photoSharing.entity.travelimage;
import com.photoSharing.entity.traveluser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: Project
 * @description: 处理图片详情
 * @author: Shen Zhengyu
 * @create: 2020-07-14 21:22
 **/
@WebServlet(name = "PhotoDetailServlet", urlPatterns = "/PhotoDetailServlet")
public class PhotoDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        int ImageID = Integer.parseInt(req.getParameter("ImageID"));
        ImageDao imageDao = new ImageDao();
        UserDao userDao = new UserDao();
        travelimage ti =  imageDao.findById(ImageID);
        if (null == ti){
            ti = imageDao.findById(1);
        }
        traveluser tu = userDao.findById(ti.getUID());
        req.setAttribute("travelimage",ti);
        req.setAttribute("traveluser",tu);
        ImageFavorDao imageFavorDao = new ImageFavorDao();
        if (req.getSession().getAttribute("traveluser") != null && imageFavorDao.isFavor(((traveluser)req.getSession().getAttribute("traveluser")).getUID(),ti.getImageID())){
            req.setAttribute("isfavor","true");
        }else{
            req.setAttribute("isfavor","false");

        }
        System.out.println(req.getAttribute("isfavor"));
        req.getRequestDispatcher("detail.jsp").forward(req,resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
