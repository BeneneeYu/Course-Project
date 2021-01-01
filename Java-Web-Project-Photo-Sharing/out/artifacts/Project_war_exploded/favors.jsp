<%@ page import="java.util.List" %>
<%@ page import="com.photoSharing.entity.travelimage" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %><%--
  Created by IntelliJ IDEA.
  User: Shen Zhengyu
  Date: 2020/7/14
  Time: 15:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>我的收藏</title>
    <!-- 引入 Bootstrap -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<%
    List<travelimage> travelimages = (List<travelimage>) request.getAttribute("travelimages");
    String UserName = (String) request.getAttribute("UserName");
%>
<script language="JavaScript" type="text/javascript"
        src="https://lib.sinaapp.com/js/jquery/2.0.2/jquery-2.0.2.min.js"></script>
<script language="JavaScript" type="text/javascript">
    var totalpage;
    var objarray = new Array();

    function init() {
        <%
if(travelimages!=null)
{
for(int i=0;i<travelimages.size();i++)
{
%>
        objarray[<%=i%>] = (<%=JSONObject.toJSON(travelimages.get(i))%>);

        <% }
        }
        %>
        totalPage = (objarray.length / 9) + 1;
        var ul = document.getElementById("fy");
        var i = 1;
        while (i <= totalPage) {
            var li = document.createElement('li')
            var a = document.createElement('button')
            a.innerHTML = i;
            a.id = i;
            a.onclick = function () {
                getPage(this.id)
            }
            li.appendChild(a)
            ul.appendChild(li)
            i++;
        }
        getPage(1);
    }

    function getPage(num) {
        // var obj = ;
        var obj = "<div class=\"container\"><div class=\"row clearfix\"><div class=\"col-md-12 column\">";
        for (i = num * 9 - 9; i < Math.min(num * 9, objarray.length); i++) {
            //每行第一个
            if (i % 3 === 0) {
                obj += "<div class=\"row clearfix\">";

            }
            obj += "<div class=\"col-md-4 column\">\n" +
                "<img alt=\"140x140\" width=\"200\" height=\"200\" src=\"" + `<%=request.getContextPath()%>` + "/PhotoServlet?Path=" + objarray[i].pATH + "\" />\n" +
                "<a href=\"PhotoDetailServlet?ImageID=" + objarray[i].imageID + "\"><h4>\n" + objarray[i].title
                +
                "</h4></a>";

            if ("<%=request.getAttribute("canAdd")%>" !== "no") {
                obj += "<a  href=\"FavorServlet?method=delete&ImageID=" + objarray[i].imageID + "\"><h4>\n" + "删除收藏"
                    +
                    "</h4></a>\n"
                obj += "</div>";

            }else {
                obj += "</div>";

            }
            if (i % 3 === 2 || i === Math.min(num * 9, objarray.length) - 1) {
                obj += "</div>"
            }
        }
        obj += "</div>\n" +
            "\t</div>\n" +
            "</div>"
        document.getElementById("show").innerHTML = obj;
    }
</script>
<body onload="init()">
<%@ include file="header.jsp" %>
<div class="container">
    <div <%
        if ("no".equals(request.getAttribute("canAdd"))) {
            out.print("style=\"display: none\"");
        }
    %>>
        <div class="row clearfix">
            <a href="FootprintServlet"><h4>我的足迹</h4></a>
        </div>
        <div class="row clearfix">
            <h4>
                <%
                    traveluser tu = (traveluser) request.getSession().getAttribute("traveluser");
                    if (null != tu) {
                        if (tu.getState() == 1) {
                            out.print("您当前设置为：收藏可被查看");
                        } else {
                            out.print("您当前设置为：收藏不可被查看");

                        }
                    }
                %>
            </h4>
        </div>
        <div class="row clearfix">
            <a class="button" href="user?method=updateState"><h4>更改我的展示状态</h4></a>
        </div>
        <div class="row clearfix">

            <form class="form-vertical" role="form" action="FriendServlet" method="get">
                <div class="form-group">
                    <label for="addFriend" class="col-sm-2 control-label">
                        <h4>添加好友：</h4>
                        <%
                            if ("notFound".equals(request.getAttribute("message"))) {
                                out.print("<h4 style=\"color: red\">没有此用户</h4>\n");
                            } else if ("success".equals(request.getAttribute("message"))) {
                                out.print("<h4 style=\"color: green\">添加成功</h4>\n");
                            } else if ("self".equals(request.getAttribute("message"))) {
                                out.print("<h4 style=\"color: red\">不能添加自己</h4>\n");
                            } else if ("failed".equals(request.getAttribute("message"))) {
                                out.print("<h4 style=\"color: red\">添加失败</h4>\n");

                            } else if ("duplicate".equals(request.getAttribute("message"))) {
                                out.print("<h4 style=\"color: red\">你们已经是好友</h4>\n");
                            }
                        %>
                    </label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" required="required" name="UserName" id="addFriend">
                    </div>
                    <label>
                        <input type="text" value="create" name="method" style="display: none"/>
                    </label>
                    <div class="form-group">
                        <div class="col-sm-2">
                            <input type="submit" class="btn btn-default" value="添加">
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="row clearfix">

        <h3><%=UserName%>的收藏：</h3>
    </div>
    <div class="row clearfix">
        <div class="col-md-12 column" id="show">

        </div>
    </div>
    <br>
    <br>
    <br>
    <br>

    <div class="row clearfix">
        <div class="col-md-12 column">
            <ul class="pagination" id="fy" style="align-content: center">

            </ul>
        </div>
    </div>
    <%@ include file="footer.jsp" %>
</div>

</body>
</html>
