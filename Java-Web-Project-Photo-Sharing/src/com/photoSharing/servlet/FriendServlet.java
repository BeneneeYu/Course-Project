package com.photoSharing.servlet;

import com.photoSharing.dao.FriendDao;
import com.photoSharing.dao.UserDao;
import com.photoSharing.entity.friendship;
import com.photoSharing.entity.traveluser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @program: Project
 * @description: 查看某人的好友
 * @author: Shen Zhengyu
 * @create: 2020-07-15 15:45
 **/
@WebServlet(name = "FriendServlet", urlPatterns = "/FriendServlet")
public class FriendServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        req.setCharacterEncoding("utf-8");

        if (null != method) {
            FriendDao friendDao = new FriendDao();
            UserDao userDao = new UserDao();
            if ("create".equals(method)) {
                String UserName = req.getParameter("UserName");
                traveluser tu = (traveluser) req.getSession().getAttribute("traveluser");
                traveluser toAdd = userDao.findByUsername(UserName);
                if (null == toAdd) {
                    req.getRequestDispatcher("FavorServlet?method=retrieve&message=notFound").forward(req, resp);
                } else if (tu.getUID() == toAdd.getUID()) {
                    req.getRequestDispatcher("FavorServlet?method=retrieve&message=self").forward(req, resp);
                } else {
                    if (friendDao.isFriend(tu.getUID(), toAdd.getUID())) {
                        req.getRequestDispatcher("FavorServlet?method=retrieve&message=duplicate").forward(req, resp);
                    } else {
                        if (friendDao.addFriend(tu.getUID(), toAdd.getUID())) {
                            req.getRequestDispatcher("FavorServlet?method=retrieve&message=success").forward(req, resp);
                        } else {
                            req.getRequestDispatcher("FavorServlet?method=retrieve&message=failed").forward(req, resp);

                        }
                    }
                }
            }else if ("retrieve".equals(method)){
                traveluser tu = (traveluser) req.getSession().getAttribute("traveluser");
                ArrayList<friendship> friendships = new ArrayList<>(friendDao.findFriendsByUID(tu.getUID()));
                ArrayList<traveluser> frineds = new ArrayList<>();
                for (friendship friendship : friendships) {
                    frineds.add(userDao.findById(friendship.getFriendTwo()));
                }
                req.setAttribute("friends",frineds);
                req.getRequestDispatcher("friends.jsp").forward(req,resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       doGet(req, resp);
    }
}
