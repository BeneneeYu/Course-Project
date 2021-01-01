package com.photoSharing.servlet;

import com.photoSharing.dao.ChatDao;
import com.photoSharing.dao.UserDao;
import com.photoSharing.entity.chathistory;
import com.photoSharing.entity.traveluser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @program: Project
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-07-17 09:07
 **/
@WebServlet(name="chat",urlPatterns = "/chat")
public class ChatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String method = req.getParameter("method");

        ChatDao chatDao = new ChatDao();
        if ("retrieve".equals(method)){
            String UID = req.getParameter("UID");
            int UID2 = ((traveluser)req.getSession().getAttribute("traveluser")).getUID();
            List<chathistory> chathistoryList = chatDao.findAllByUID(Integer.parseInt(UID),UID2);
            req.setAttribute("chathistoryList",chathistoryList);
            UserDao userDao = new UserDao();
            req.setAttribute(UID,userDao.findById(Integer.parseInt(UID)).getUserName());
            req.setAttribute(String.valueOf(UID2),userDao.findById(UID2).getUserName());

            req.getRequestDispatcher("chat.jsp").forward(req,resp);
        }else if ("create".equals(method)){
            String UID = req.getParameter("UID");
            int UID2 = ((traveluser)req.getSession().getAttribute("traveluser")).getUID();
            String content = req.getParameter("content");
            if (null != content) {
                chatDao.insertChathistory(UID2, Integer.parseInt(UID), content);
            }
            resp.sendRedirect("chat?method=retrieve&UID=" + UID);
        }
    }
}
