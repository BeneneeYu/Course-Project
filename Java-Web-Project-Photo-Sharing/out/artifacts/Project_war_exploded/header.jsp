<%@ page import="com.photoSharing.entity.traveluser" %><%--
  Created by IntelliJ IDEA.
  User: 86460
  Date: 2020/7/11
  Time: 15:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>header</title>
    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>

    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container"  >
    <div class="row clearfix">
        <div class="col-md-12 column">
            <nav class="navbar navbar-default" role="navigation">
                <div class="navbar-header">
                    <button type="button"  class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button> <a class="navbar-brand" href="index">旅行图片分享平台</a>
                <%
                  if ((session.getAttribute("loginMessage")) == "success") {
                      out.print("                    <button type=\"button\"  class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#bs-example-navbar-collapse-1\"> <span class=\"sr-only\">Toggle navigation</span><span class=\"icon-bar\"></span><span class=\"icon-bar\"></span><span class=\"icon-bar\"></span></button> <a class=\"navbar-brand\" href=\"index\" style=\"color: green\">登录成功</a>\n");
                      session.setAttribute("loginMessage",null);
                  }
                %>
                </div>

                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <a type="button" class="btn btn-default" href="index">首页</a>
                        </li>
                        <li>
                            <a type="button" class="btn btn-default" href="searchPhoto.jsp">搜索</a>
                        </li>

                        <li class="dropdown" id="logged" <%
                            if (null == session.getAttribute("traveluser")){
                                out.print("style=\"display: none\"");
                            }
                        %>>
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><%
                                if (null != session.getAttribute("traveluser")){
                                    out.print(((traveluser)session.getAttribute("traveluser")).getUserName());
                                }
                            %><strong class="caret"></strong></a>
                            <ul class="dropdown-menu">
                                <li>
                                    <a href="FavorServlet?method=retrieve">我的收藏</a>
                                </li>
                                <li>
                                    <a href="PhotoOperationServlet?method=upload">上传</a>
                                </li>
                                <li>
                                    <a href="user?method=retrieve">我的图片</a>
                                </li>
                                <li>
                                    <a href="FriendServlet?method=retrieve">我的好友</a>
                                </li>
                                <li>
                                    <a href="LogoutServlet">退出登录</a>
                                </li>
                            </ul>
                        </li>
                        <li id="unlogged" <%
                            if (null != session.getAttribute("traveluser")){
                                out.print("style=\"display: none\"");
                            }
                        %>>
                            <a type="button" class="btn btn-default" href="login.jsp">登录</a>

                        </li>
                    </ul>
                </div>

            </nav>
        </div>
    </div>


















<%--    <div class="row clearfix">--%>
<%--        <div class="col-md-12 column">--%>
<%--            <div class="row clearfix">--%>
<%--                <div class="col-md-8 column">--%>
<%--                </div>--%>
<%--                <div class="col-md-4 column">--%>
<%--                    <div class="row clearfix">--%>
<%--                            <a type="button" class="btn btn-default" href="index.jsp">首页</a>--%>
<%--                            <a type="button" class="btn btn-default" href="searchPhoto.jsp">搜索</a>--%>
<%--&lt;%&ndash;                         <a type="button" class="btn btn-default" href="login.jsp">登陆</a>&ndash;%&gt;--%>
<%--                        <div class="btn-group">--%>
<%--                            <button class="btn btn-default">用户</button>--%>
<%--                            <button data-toggle="dropdown" class="btn btn-default dropdown-toggle">--%>
<%--                                <span class="caret"></span></button>--%>
<%--                        <ul class="dropdown-menu">--%>
<%--                            <li>--%>
<%--                                <a href="collection.jsp">我的收藏</a>--%>
<%--                            </li>--%>
<%--                            <li>--%>
<%--                                <a href="uploadPhoto.jsp">上传</a>--%>
<%--                            </li>--%>
<%--                            <li>--%>
<%--                                <a href="myPhotos.jsp">我的图片</a>--%>
<%--                            </li>--%>
<%--                            <li>--%>
<%--                                <a href="#">我的好友</a>--%>
<%--                            </li>--%>
<%--                            <li>--%>
<%--                                <a href="#">退出登录</a>--%>
<%--                            </li>--%>
<%--                        </ul>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
</div>
</body>
</html>
