<%@ page import="com.photoSharing.entity.traveluser" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: Shen Zhengyu
  Date: 2020/7/16
  Time: 12:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>我的好友</title>
    <!-- 引入 Bootstrap -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>
<%@ include file="header.jsp"%>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">


            <%
    ArrayList<traveluser> friends = (ArrayList<traveluser>) request.getAttribute("friends");
    if (null == friends || friends.size() == 0){
        out.print("<h4>您暂时没有好友</h4>");
    }else{
        for (traveluser friend : friends) {
            out.print("            <ul>\n" +
"<li>用户名：<a href=\"FavorServlet?method=retrieve&UID=" + friend.getUID()+"\">"+friend.getUserName() +"</a></li>" +
                    "                <li>\n" +
                    "                    邮箱："+  friend.getEmail()+"\n" +
                    "                </li>\n" +
                    "                <li>\n" +
                    "                    注册时间："+ friend.getDateJoined()+"\n" +
            "                </li>\n" +
                    "<li>\n" +
                    "<a href=\"chat?method=create&UID=" + friend.getUID()+"\">聊天</a>"
                    + "</li>\n"+

            "            </ul>");
        }
    }
%>
        </div>
    </div>
    <%@ include file="footer.jsp"%>

</div>

</body>
</html>
