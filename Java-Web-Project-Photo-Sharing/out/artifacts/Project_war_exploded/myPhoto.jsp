<%@ page import="com.photoSharing.entity.travelimage" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %><%--
  Created by IntelliJ IDEA.
  User: Shen Zhengyu
  Date: 2020/7/16
  Time: 14:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<travelimage> travelimages = new ArrayList<>();
    travelimages = (ArrayList<travelimage>) request.getAttribute("travelimages");

%>
<html>
<head>
    <title>我的照片</title>
    <!-- 引入 Bootstrap -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<script language="JavaScript" type="text/javascript"
        src="https://lib.sinaapp.com/js/jquery/2.0.2/jquery-2.0.2.min.js"></script>
<script language="JavaScript" type="text/javascript">
    var totalpage;
    var objarray = new Array();

    function init() {
        <%
        if(travelimages==null){return;}
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
            a.onclick = function(){
                getPage(this.id)
            }
            li.appendChild(a)
            ul.appendChild(li)
            i++;
        }
        getPage(1);
    }
    function todelete(obj) {
        var message = confirm("你确定要删除吗?")
        if(message===true)
        {
            window.location.href="PhotoOperationServlet?method=delete&ImageID=" + obj.id;
        }
    }
    function getPage(num) {
        // var obj = document.getElementById("show");

        var obj = "<div class=\"container\">\t<div class=\"row clearfix\"><div class=\"col-md-12 column\">";
        for (i = num*9-9; i < Math.min(num*9,objarray.length); i++) {
            //每行第一个
            if (i % 3 === 0){
                obj += "<div class=\"row clearfix\">";
            }
            obj += "<div class=\"col-md-4 column\">\n" +
                "<img alt=\"140x140\" width=\"200\" height=\"200\" src=\"" + `<%=request.getContextPath()%>` + "/PhotoServlet?Path=" + objarray[i].pATH +"\" />\n" +
                "<a href=\"PhotoDetailServlet?ImageID=" + objarray[i].imageID + "\"><h4>\n" + objarray[i].title
                +
                "</h4></a>\n" +
                "<a href=\"PhotoOperationServlet?method=update&ImageID=" + objarray[i].imageID + "\"><h4>\n" + "修改"
                +
                "</h4></a>\n" +
                "<a class=\"delete\" onclick=\"todelete(this)\" id=\"" + objarray[i].imageID +"\"><h4>\n" + "删除"
                +
                "</h4></a>\n" +
                "</div>"
            if (i % 3 === 2 || i === Math.min(num*9,objarray.length)-1){
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
    <h3>我上传的图片：</h3>


    <div class="container">
        <div class="row clearfix">
            <div class="col-md-12 column" id="show">
            </div>
        </div>
    </div>
    <br>
    <br>
    <br>
    <br>
        <div class="container">

            <div class="row clearfix">
                <div class="col-md-12 column">
                    <ul class="pagination" id="fy">
                    </ul>
                </div>
            </div>
        </div>
    <%@ include file="footer.jsp" %>
</div>
</body>
</html>
