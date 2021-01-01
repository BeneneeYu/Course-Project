package com.photoSharing.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @program: Project
 * @description: 所有访问页面的请求都会被要求登录
 * @author: Shen Zhengyu
 * @create: 2020-07-19 14:20
 **/
@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        ArrayList<String> excluded = new ArrayList<>();
        excluded.add(req.getContextPath() + "/login.jsp");
        excluded.add(req.getContextPath() + "/register.jsp");
        excluded.add(req.getContextPath() + "/images/loginBackground.jfif");
        excluded.add(req.getContextPath() + "/index");
        excluded.add(req.getContextPath() + "/images/registerBackground.jfif");
        excluded.add(req.getContextPath() + "/validate.jsp");
        excluded.add(req.getContextPath() + "/PhotoServlet");
        excluded.add(req.getContextPath() + "/LoginServlet");
        excluded.add(req.getContextPath() + "/RegisterServlet");
        excluded.add(req.getContextPath() + "/PhotoDetailServlet");
        excluded.add(req.getContextPath() + "/detail.jsp");
        excluded.add(req.getContextPath() + "/searchPhoto.jsp");
        excluded.add(req.getContextPath() + "/SearchPhotoServlet");

        System.out.println(req.getRequestURI());
        String url = req.getRequestURI();
        if (excluded.contains(url)){
            filterChain.doFilter(req, resp);
        }else {
            if (req.getSession().getAttribute("traveluser") == null) {
                // 未登录，自动跳转到登录页:
                System.out.println("AuthFilter: not signin!");
                resp.sendRedirect(req.getContextPath() + "/login.jsp");
            } else {
                // 已登录，继续处理:
                filterChain.doFilter(req, resp);

            }
        }
    }
}
