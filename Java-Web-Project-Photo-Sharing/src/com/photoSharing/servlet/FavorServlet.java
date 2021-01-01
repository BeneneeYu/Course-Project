package com.photoSharing.servlet;

import com.photoSharing.dao.ImageDao;
import com.photoSharing.dao.ImageFavorDao;
import com.photoSharing.dao.UserDao;
import com.photoSharing.entity.travelimage;
import com.photoSharing.entity.travelimagefavor;
import com.photoSharing.entity.traveluser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @program: Project
 * @description: 增加收藏
 * @author: Shen Zhengyu
 * @create: 2020-07-14 15:34
 **/
@WebServlet(name = "FavorServlet", urlPatterns = "/FavorServlet")
public class FavorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        String method = req.getParameter("method");
        ImageFavorDao imageFavorDao = new ImageFavorDao();
        ImageDao imageDao = new ImageDao();
        UserDao userDao = new UserDao();
        traveluser tu = (traveluser) req.getSession().getAttribute("traveluser");
        if (null == tu) {
            resp.sendRedirect("index");
        }else{
            int UID = tu.getUID();
            if ("create".equals(method)) {
                String ImageID = req.getParameter("ImageID");
                travelimagefavor tf = new travelimagefavor();
                tf.setImageID(Integer.parseInt(ImageID));
                tf.setUID(UID);
                imageFavorDao.addFavor(tf);
                imageDao.addHeat(tf.getImageID());
                resp.sendRedirect("FavorServlet?method=retrieve");
            }else if("delete".equals(method)){
                //执行收藏的删除操作

                String ImageID = req.getParameter("ImageID");
                travelimagefavor tif = new travelimagefavor();
                tif.setUID(UID);
                tif.setImageID(Integer.parseInt(ImageID));
                imageFavorDao.deleteFavor(tif);
                imageDao.reduceHeat(tif.getImageID());
                resp.sendRedirect("FavorServlet?method=retrieve");
            }else if ("retrieve".equals(method)){
                //查询指定的用户
                String TargetUID = req.getParameter("UID");
                req.setAttribute("message",req.getParameter("message"));

                if (null == TargetUID || "".equals(TargetUID)){
                    //查看自己的，不找指定的
                    ArrayList<travelimagefavor> travelimagefavors = new ArrayList<>(imageFavorDao.findAllMyFavorImages(UID));
                    ArrayList<travelimage> travelimages = new ArrayList<>();
                    for (travelimagefavor travelimagefavor : travelimagefavors) {
                        travelimages.add(imageDao.findById(travelimagefavor.getImageID()));
                    }
                    req.setAttribute("travelimages",travelimages);
                    req.setAttribute("UserName",tu.getUserName());
                    traveluser tunew = userDao.findById(UID);
                    req.getSession().setAttribute("traveluser",tunew);
                    req.getRequestDispatcher("favors.jsp").forward(req, resp);
                }else{
                    //查看指定人的
                    //先看有没有设置权限保护
                    int TargetUserID = Integer.parseInt(TargetUID);
                    traveluser targetTU = userDao.findById(TargetUserID);
                    //如果查看别人的，则不能添加好友
                    req.setAttribute("canAdd","no");
                    if (targetTU.getState() == -1){
                        req.setAttribute("UserName",targetTU.getUserName()+"设置了不可查看，所以你无法查看他");
                        req.getRequestDispatcher("favors.jsp").forward(req, resp);
                    }else{
                        ArrayList<travelimagefavor> travelimagefavors = new ArrayList<>(imageFavorDao.findAllMyFavorImages(TargetUserID));
                        ArrayList<travelimage> travelimages = new ArrayList<>();
                        for (travelimagefavor travelimagefavor : travelimagefavors) {
                            travelimages.add(imageDao.findById(travelimagefavor.getImageID()));
                        }
                        req.setAttribute("travelimages",travelimages);
                        req.setAttribute("UserName",targetTU.getUserName());

                        req.getRequestDispatcher("favors.jsp").forward(req, resp);
                    }

                }


            }

        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
