package com.photoSharing.servlet;

import com.photoSharing.dao.UserDao;
import com.photoSharing.entity.traveluser;
import com.photoSharing.utils.Base64Code;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: Project
 * @description: 登录处理Servlet
 * @author: Shen Zhengyu
 * @create: 2020-07-14 09:59
 **/
@WebServlet(name = "LoginServlet", urlPatterns = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String username = req.getParameter("UserName");
        String password = req.getParameter("Pass");
        HttpSession session = req.getSession();
        String sessionCode = (String) session.getAttribute("randomCode");
        String paramCode = req.getParameter("CHECK_CODE_PARAM_NAME");
        if(!(paramCode != null && paramCode.equals(sessionCode))){
            resp.sendRedirect("login.jsp?message=codeError");
        }else {
            if (username != null && !username.trim().equals("")) {
                //输入正确
                UserDao userDao = new UserDao();
                traveluser user = userDao.findByUsername(username);
                //找到用户
                if (null != user) {
                    //密码正确
                    if (password.equals(user.getPass())) {
                        req.getSession().setAttribute("traveluser", user);
                        req.getSession().setAttribute("loginMessage","success");
                        resp.sendRedirect("index");
                    } else {
                        //密码错误
                        resp.sendRedirect("login.jsp?message=passError" + "&UserName=" + username);
                    }
                } else {
                    //用户不存在
                    resp.sendRedirect("login.jsp?message=userError");
                }
            } else {
                //输入不正确
                resp.sendRedirect("login.jsp?message=empty");
            }
        }
    }
}
