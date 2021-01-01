<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.TreeSet" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.File" %>
<%@ page import="com.photoSharing.dao.GeoCountriesRegionsDao" %>
<%@ page import="com.photoSharing.entity.*" %>
<%@ page import="com.photoSharing.dao.GeocitiesDao" %><%--
  Created by IntelliJ IDEA.
  User: 86460
  Date: 2020/7/10
  Time: 19:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        .outer{
            position:relative;
            margin: 50px auto;
        }
        .outer:after{
            content: "\200B";
            display:block;
            height:0;
            clear:both;
        }
        .outer>.minbox{
            width:400px;
            height:200px;
            border:1px solid #888888;
            float: left;
            position: relative;
        }
        .outer>.minbox>img{
            width:400px;
            height:200px;
        }
        .outer>.minbox>.mask{
            width:100px;
            height:100px;
            background-color: rgba(135, 223, 170, 0.58);
            position:absolute;
            top:0;
            left:0;
            display:none;
        }
        .outer>.bigbox{
            width:250px;
            height:250px;
            position: absolute;
            left:402px;
            top:0px;
            overflow: hidden;
            display:none;
        }
        .outer>.bigbox>img{
            width:800px;
            height:400px;
        }
    </style>
    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <title>图片详情</title>
</head>
<%
    travelimage ti = new travelimage();
    traveluser tu = new traveluser();
    if(null != request.getAttribute("travelimage") && null != request.getAttribute("traveluser")) {
         ti = (travelimage) request.getAttribute("travelimage");
        tu = (traveluser) request.getAttribute("traveluser");
        if (null == session.getAttribute("footprint")){
            ArrayList<String> footprint = new ArrayList<>();
            session.setAttribute("footprint",footprint);
        }
        ArrayList<String> footprint = (ArrayList<String>) session.getAttribute("footprint");
        footprint.remove(String.valueOf(ti.getImageID()));
        footprint.add(String.valueOf(ti.getImageID()));
        if (footprint.size() == 11){
            footprint.remove(0);
        }


    }else{
        response.sendRedirect("index");
    }


%>
<body>
<%@ include file="header.jsp"%>
<div class="container">
    <h3>图片详情</h3>
    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="row clearfix">
                <div class="col-md-6 column">
                    <div class="outer">
                        <div class="minbox">
                    <img alt="140x140" src="<%=request.getContextPath()%>/PhotoServlet?Path=<%= ti.getPATH()%>"/>
                            <p class="mask"></p>
                        </div>
                        <div class="bigbox">
                            <img alt="140x140" src="<%=request.getContextPath()%>/PhotoServlet?Path=<%= ti.getPATH()%>" />
                        </div>
                    </div>

                <%--                    <img alt="140x140" src="<%=request.getContextPath()%>/PhotoServlet?Path=<%= ti.getPATH()%>" width="500px" height="309px"/>--%>
                    <a type="button" class="btn btn-default"  href="FavorServlet?method=create&ImageID=<%=ti.getImageID()%>"  <%
                        if (null == request.getSession().getAttribute("traveluser") || "true".equals((String)request.getAttribute("isfavor"))){
                            out.print("style=\"display:none\"");
                        }

                        %>>增加收藏</a>
                    <a type="button" class="btn btn-default"  href="FavorServlet?method=delete&ImageID=<%=ti.getImageID()%>"  <%
                        if (null == request.getSession().getAttribute("traveluser") || "false".equals((String)request.getAttribute("isfavor"))){
                            out.print("style=\"display:none\"");
                        }
                    %>>取消收藏</a>
                    <a type="button" class="btn btn-default" href="index">首页</a>

                </div>
                <div class="col-md-6 column" style="font-size: 18pt">
                    <dl>
                        <dt>
                            作者
                        </dt>
                        <dd>
                            <%
                                if (null == ti.getAuthor()){
                                    out.print("佚名");
                                }else{
                                    out.print(ti.getAuthor());
                                }
                            %>
                        </dd>
                        <dt>
                            标题
                        </dt>
                        <dd>
                            <%=ti.getTitle()%>
                        </dd>
                        <dt>
                            图片主题
                        </dt>
                        <dd>
                            <%=ti.getContent()%>
                        </dd>
                        <dt>
                            简介
                        </dt>
                        <dd>
                            <%=ti.getDescription()%>
                        </dd>
                        <dt>
                            热度
                        </dt>
                        <dd>
                            <%=ti.getHeat()%>
                        </dd>
                        <dt>
                            国家
                        </dt>
                        <dd>
                            <%
                                GeoCountriesRegionsDao gd = new GeoCountriesRegionsDao();
                                geocountries_regions gr = gd.findByCountryByISO(ti.getCountry_RegionCodeISO());
                                out.print(gr.getCountry_RegionName());
                            %>
                        </dd>
                        <dt>
                            城市
                        </dt>
                        <dd>
                            <%
                                GeocitiesDao gdd = new GeocitiesDao();
                                geocities gcc = gdd.findByCityCode(ti.getCityCode());
                                out.print(gcc.getAsciiName());

                            %>
                        </dd>
                        <dt>
                            发布时间
                        </dt>
                        <dd>
                            <%=ti.getDateUpdated()%>
                        </dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>
    <%@ include file="footer.jsp"%>

</div>
</body>
</html>
<script>
    oMinBox = document.querySelector('.outer>.minbox')
    oMask = document.querySelector('.outer>.minbox>.mask')
    oBigBox = document.querySelector('.outer>.bigbox')
    oImg = document.querySelector('.outer>.bigbox>img')

    oMinBox.onmouseenter = function (e){
        var e = e || window.event
        oMask.style.display = "block"
        oBigBox.style.display = "block"
        oMinBox.onmousemove = function(e){
            var e = e || window.event
            var maskPosition = {
                left:e.clientX - oMask.offsetWidth/2 - oMinBox.getBoundingClientRect().left,
                top:e.clientY - oMask.offsetHeight/2 - oMinBox.getBoundingClientRect().top
            }
            if(maskPosition.left<=0){
                maskPosition.left = 0
            }else if(maskPosition.left>=oMinBox.clientWidth - oMask.offsetWidth){
                maskPosition.left = oMinBox.clientWidth - oMask.offsetWidth
            }
            if(maskPosition.top<=0){
                maskPosition.top = 0
            }else if(maskPosition.top>=oMinBox.clientHeight - oMask.offsetHeight){
                maskPosition.top = oMinBox.clientHeight - oMask.offsetHeight
            }
            oMask.style.left = maskPosition.left + 'px'
            oMask.style.top = maskPosition.top + 'px'

            //计算比例
            var scaleWidth = (oImg.clientWidth - oBigBox.offsetWidth)/(oMinBox.clientWidth - oMask.offsetWidth)
            var scaleHeight = (oImg.clientHeight - oBigBox.offsetHeight)/(oMinBox.clientHeight - oMask.offsetHeight)
            var bigBoxPosition = {
                left:maskPosition.left * scaleWidth,
                top:maskPosition.top * scaleHeight
            }
            oImg.style.marginLeft = -bigBoxPosition.left + 'px'
            oImg.style.marginTop = -bigBoxPosition.top + 'px'
        }
    }
    oMinBox.onmouseleave = function(e){
        var e = e || window.event
        oMask.style.display = "none"
        oBigBox.style.display = "none"
    }
</script>