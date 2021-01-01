<%@ page import="java.awt.image.BufferedImage" %>
<%@ page import="java.awt.*" %>
<%@ page import="java.util.Random" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="javax.imageio.ImageIO" %><%--
  Created by IntelliJ IDEA.
  User: 86460
  Date: 2020/7/11
  Time: 1:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <script type="text/javascript" src="http://www.daimajiayuan.com/download/jquery/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="http://cdn.bootcss.com/bootstrap-select/2.0.0-beta1/js/bootstrap-select.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

    <link href="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet">
    <script src="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
    <title>登录</title>
<style type="text/css">
    body{
        background:url("images/loginBackground.jfif") no-repeat;
        background-size: 100%;
    }

</style>
</head>

<script>
    function loadimage() {
        document.getElementById("validationCode_img").src = "validate.jsp?time=" + new Date().getTime();
    }
    function give() {
        document.getElementById("inputPass").value = encode64(document.getElementById("inputPassword").value);
    }
    function encode64(input) {
        var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv" + "wxyz0123456789+/" + "=";
        var output = "";
        var chr1, chr2, chr3 = "";
        var enc1, enc2, enc3, enc4 = "";
        var i = 0;
        do {
            chr1 = input.charCodeAt(i++);
            chr2 = input.charCodeAt(i++);
            chr3 = input.charCodeAt(i++);
            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;
            if (isNaN(chr2)) {
                enc3 = enc4 = 64;
            } else if (isNaN(chr3)) {
                enc4 = 64;
            }
            output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2)
                + keyStr.charAt(enc3) + keyStr.charAt(enc4);
            chr1 = chr2 = chr3 = "";
            enc1 = enc2 = enc3 = enc4 = "";
        } while (i < input.length);
        return output;
    }
</script>
<body>

<%@ include file="header.jsp"%>
<div class="container"  >
    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="row clearfix">
                <div class="col-md-8 column">
                </div>
                <div class="col-md-4 column">
                    <div class="row clearfix">
                        <div class="col-md-12 column">
                            <br>
                            <br>
                            <br>
                            <br>

                            <%
                                String color = "black";
                                String title = "请先登录";
                                String message = request.getParameter("message");
                                if (null != request.getParameter("message")){
                                    if( message.equals("userError")){
                                        title="登录失败,用户名和密码错误";
                                    }else if(message.equals("empty")){
                                        title="登录失败,用户名或密码为空";
                                    }else if(message.equals("passError")){
                                        title="登录失败,用户名和密码错误";
                                    }else if(message.equals("codeError")){
                                        title="登录失败,验证码错误";
                                    }
                                    color="red";


                                }
                            %>
                            <h4 style="color:<%=color%>"><%=title%></h4>
                            <form class="form-horizontal" role="form" action="LoginServlet" method="post">
                                <div class="form-group">
                                    <label for="inputUsername" class="col-sm-3 control-label">用户名</label>
                                    <div class="col-sm-9">
                                        <input type="text" class="form-control" required="required" name="UserName" id="inputUsername" value="<%
                                        if (null != request.getParameter("UserName")&& !"".equals(request.getParameter("UserName"))){
                                            out.print(request.getParameter("UserName"));
                                        }

                                        %>"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="inputPassword" class="col-sm-3 control-label">密码</label>
                                    <div class="col-sm-9">
                                        <input type="password" class="form-control" required="required" id="inputPassword" onblur="give()" />
                                    </div>
                                </div>
                                <label>
                                    <input type="text" style="display: none" name="Pass" id="inputPass">
                                </label>
                                <div class="form-group">
                                    <label for="CHECK_CODE_PARAM_NAME" class="col-sm-3 control-label">验证</label>
                                    <div class="col-sm-9">
                                        <input type="text" class="form-control" required="required" name="CHECK_CODE_PARAM_NAME" id="CHECK_CODE_PARAM_NAME" minlength="4" maxlength="4" />
                                        <img alt="验证码" src="validate.jsp" onclick="loadimage();return false;" >

                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-offset-9 col-sm-3">
                                        <input type="submit" class="btn btn-default" value="登录">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-offset-9 col-sm-3">
                                        <a href="register.jsp" class="btn btn-default">注册</a>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>