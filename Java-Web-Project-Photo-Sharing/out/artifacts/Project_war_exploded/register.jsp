<%--
  Created by IntelliJ IDEA.
  User: 86460
  Date: 2020/7/11
  Time: 1:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <title>注册</title>
    <style type="text/css">
        body{
            background:url("images/registerBackground.jfif") no-repeat;
            background-size: 100%;
        }

    </style>
</head>
<script type="text/javascript">
    function loadimage(){
        document.getElementById("validationCode_img").src= "validate.jsp?time=" + new Date().getTime();
    }
    function give() {
        document.getElementById("inputPass").value = encode64(document.getElementById("inputPassword").value);
    }
    function checkStrength() {
        var tex = document.getElementById("strength");
        if(document.getElementById("inputPassword").value.length >= 9){
            tex.innerText = "高";
            tex.style.color = "green";
        }
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

<%
    if (null != session.getAttribute("traveluser")){
        response.sendRedirect("index");
    }

%>
<br>
<br>
<br>
<br>
<div class="container">
    <div class="row clearfix">

    </div>
    <div class="row clearfix">
        <div class="col-md-12 column">
            <p>
                <br>
                注意事项：<br>
                ●邮箱格式需要正确<br>
                ●密码需要与确认密码相同<br>
                ●用户名长度 4 至 15 位，包含 4 和 15。<br>
                ●密码长度 6 至 12 位，包含 6 和 12<br>
                <%
                    if (null != request.getParameter("message")) {
                        if (request.getParameter("message").equals("duplicate")) {
                            out.print("<h4 style=\"color:red\">重复注册</h4>");
                        }else if(request.getParameter("message").equals("codeError")){
                            out.print("<h4 style=\"color:red\">验证码错误</h4>");

                        }
                    }
                %>
            </p>
            <form class="form-horizontal" role="form" action="RegisterServlet" method="post">
                <div class="form-group">
                    <label for="inputUsername" class="col-sm-2 control-label">用户名</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="inputUsername" name="UserName" required="required" value="<%
                        if (null != request.getParameter("UserName")){
                            out.print(request.getParameter("UserName"));
                        }
                        %>" minlength="4" maxlength="15"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputEmail" class="col-sm-2 control-label">邮箱</label>
                    <div class="col-sm-10">
                        <input type="email" class="form-control" id="inputEmail" name="Email" value="<%
                        if (null != request.getParameter("Email")){
                            out.print(request.getParameter("Email"));
                        }
                        %>" required="required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputPassword" class="col-sm-2 control-label">密码<text id="strength" style="color: red">弱</text></label>
                    <div class="col-sm-10">
                        <input type="password" oninput="checkStrength()" class="form-control" id="inputPassword"  required="required" minlength="6" maxlength="12" onblur="give()"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="checkPassword" class="col-sm-2 control-label">确认密码</label>
                    <div class="col-sm-10">
                        <input type="password" class="form-control" id="checkPassword"  required="required" minlength="6" maxlength="12" onblur="ValPwd()"/>
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
                    <div class="col-sm-offset-11 col-sm-1" id="registerButtonDiv">
                        <input type="submit" class="btn btn-default" id="registerButton" value="注册" >
                    </div>
                </div>
                <p id="message"></p>
            </form>
        </div>
    </div>
</div>

</body>
</html>
<script type="text/javascript">
    function ValPwd(){
        var a = document.getElementById("inputPassword").value;
        console.log(a);
        var b = document.getElementById("checkPassword").value;
        console.log(b);
        if(a!==b){
            document.getElementById("registerButton").value = "两次密码不一致，修改后注册";
            document.getElementById("registerButtonDiv").classList.remove("col-sm-offset-11");
            document.getElementById("registerButtonDiv").classList.remove("col-sm-1");
            document.getElementById("registerButtonDiv").classList.add("col-sm-offset-10");
            document.getElementById("registerButtonDiv").classList.add("col-sm-2");
            document.getElementById("checkPassword").value = "";


        }
    }
</script>