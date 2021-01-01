package com.photoSharing.servlet;

import com.photoSharing.dao.UserDao;
import com.photoSharing.entity.traveluser;
import com.photoSharing.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * @program: Project
 * @description: 处理注册
 * @author: Shen Zhengyu
 * @create: 2020-07-14 10:42
 **/
@WebServlet(name = "RegisterServlet", urlPatterns = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        HttpSession session = req.getSession();
        String sessionCode = (String) session.getAttribute("randomCode");
        String paramCode = req.getParameter("CHECK_CODE_PARAM_NAME");
        if (!(paramCode != null && paramCode.equals(sessionCode))) {
            resp.sendRedirect("register.jsp?message=codeError");
        } else {
            WebUtils webUtils = new WebUtils();
            Date date = webUtils.getBJTime();
            String UserName = req.getParameter("UserName");
            String Email = req.getParameter("Email");
            String Pass = req.getParameter("Pass");
            traveluser tu = new traveluser();
            tu.setUserName(UserName);
            tu.setState(1);
            tu.setPass(Pass);
            tu.setEmail(Email);
            tu.setDateLastModified(date);
            tu.setDateJoined(date);
            UserDao userDao = new UserDao();
            if (userDao.insert(tu)) {
                //插入成功
                req.getSession().setAttribute("traveluser", userDao.findByUsername(UserName));
                resp.sendRedirect("index");
            } else {

                resp.sendRedirect("register.jsp?message=duplicate" + "&UserName=" + UserName + "&Email=" + Email);
//            req.getRequestDispatcher("register.jsp").forward(req,resp);
            }
            ;
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

}
