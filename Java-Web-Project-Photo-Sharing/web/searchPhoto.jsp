<%@ page import="java.util.ArrayList" %>
<%@ page import="com.photoSharing.entity.travelimage" %>
<%@ page import="com.alibaba.fastjson.JSON" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %><%--
  Created by IntelliJ IDEA.
  User: Shen Zhengyu
  Date: 2020/7/11
  Time: 21:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ArrayList<travelimage> travelimages = new ArrayList<>();
    travelimages = (ArrayList<travelimage>) request.getAttribute("photos");
%>
<html>
<head>
    <title>搜索图片</title>
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

if(travelimages!=null)
{

%>
        initt()
        <%
        }
        %>
    }

    function initt() {

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
        var obj = "<div class=\"container\">\t<div class=\"row clearfix\">\n" +
            "\t\t<div class=\"col-md-12 column\">";
        for (i = num * 9 - 9; i < Math.min(num * 9, objarray.length); i++) {
            //每行第一个
            if (i % 3 === 0) {
                obj += "<div class=\"row clearfix\">";
            }
            obj += "<div class=\"col-md-4 column\">\n" +
                "<img alt=\"140x140\" width=\"200\" height=\"200\" src=\"" + `<%=request.getContextPath()%>` + "/PhotoServlet?Path=" + objarray[i].pATH + "\" />\n" +
                "<a href=\"PhotoDetailServlet?ImageID=" + objarray[i].imageID + "\"><h4>\n" + objarray[i].title
                +
                "</h4></a>\n" +
                "</div>"
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
    <div class="row clearfix">
        <div class="col-md-12 column">
            <form role="form" action="SearchPhotoServlet" method="post">
                <div class="form-group">

                    <label for="keyword">搜索词</label><input type="text" name="keyword" class="form-control" id="keyword"
                        <%
                            if (null != request.getParameter("keyword")) {
                                out.print("value=\"" + request.getParameter("keyword") + "\"");
                            }
                        %>
                />
                </div>
                <div class="form-group">
                    <label for="optionsRadios1">请选择按主题/标题筛选</label>
                    <div class="radio">
                        <label>
                            <input type="radio" name="choose" id="optionsRadios1" value="Content">主题
                        </label>
                    </div>
                    <div class="radio">
                        <label>
                            <input type="radio" name="choose" id="optionsRadios2" value="Title" checked>标题
                        </label>
                    </div>
                </div>
                <div class="form-group">
                    <label for="optionsRadios3">请选择按热度/时间</label>
                    <div class="radio">
                        <label>
                            <input type="radio" name="order" id="optionsRadios3" value="Heat" checked>热度
                        </label>
                    </div>
                    <div class="radio">
                        <label>
                            <input type="radio" name="order" id="optionsRadios4" value="DateUpdated">时间
                        </label>
                    </div>
                </div>

                <button type="submit" class="btn btn-default" onclick="init()">搜索</button>
            </form>
        </div>
    </div>
</div>
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
