<%@ page import="java.util.ArrayList" %>
<%@ page import="com.photoSharing.entity.travelimage" %>
<%@ page import="java.util.Collections" %><%--
  Created by IntelliJ IDEA.
  User: Shen Zhengyu
  Date: 2020/7/16
  Time: 10:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>我的足迹</title>
    <!-- 引入 Bootstrap -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<%
    ArrayList<travelimage> travelimages = (ArrayList<travelimage>) request.getAttribute("footprint");

%>
<body>
<%@ include file="header.jsp"%>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <%
                if (null == travelimages){
                    out.println("暂时没有足迹");
                }else{
                    Collections.reverse(travelimages);
                    for (travelimage travelimage : travelimages) {
                        out.println("<a href=\"PhotoDetailServlet?ImageID=" + travelimage.getImageID() + "\"><h4>" + travelimage.getTitle() + "</h4></a><br>");

                    }
                }
            %>
        </div>
    </div>
    <%@ include file="footer.jsp"%>

</div>

</body>
</html>
