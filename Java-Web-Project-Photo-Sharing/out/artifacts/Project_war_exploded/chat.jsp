<%@ page import="com.photoSharing.entity.chathistory" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Shen Zhengyu
  Date: 2020/7/17
  Time: 9:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>聊天界面</title>
    <!-- 引入 Bootstrap -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<%@ include file="header.jsp" %>
<%
    List<chathistory> chathistoryList = (List<chathistory>) request.getAttribute("chathistoryList");
%>
<div class="container">
    <%
        if (null != chathistoryList) {
            for (chathistory chathistory : chathistoryList) {
                if (null != chathistory.getContent()) {
                    out.println("<div class=\"row\">");
                    out.println("<h3>" + request.getAttribute(String.valueOf(chathistory.getSenderID())) + "在" + chathistory.getDate() + "说：</h4><br>");
                    out.println("<h4>" + chathistory.getContent() + "</h4>");
                    out.println("</div>");
                }
            }
        }else{
            out.print("<h2>暂时没有聊天</h2>");
        }
    %>
</div>
<div class="container">
    <div class="row">
    <form role="form" action="chat" method="post" >
        <input style="display: none" name="UID" value="<%=request.getParameter("UID")%>">
        <input style="display: none" name="method" value="create">

        <div class="form-group">
            <label for="content">消息</label><input type="text" class="form-control" id="content" name="content"/>
        </div>
        <button type="submit" class="btn btn-default">发送</button>
    </form>
    </div>
    <%@ include file="footer.jsp" %>
</div>
</body>
</html>
