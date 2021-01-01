<%@ page import="com.photoSharing.utils.VerifyCode" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.photoSharing.entity.travelimage" %><%--
  Created by IntelliJ IDEA.
  User: Shen Zhengyu
  Date: 2020/7/12
  Time: 14:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <style type="text/css">
    /*.item img {*/
    /*width: 300px;*/
    /*height: 200px;*/
    /*}*/
    .carousel {
      width: 1200px;
      height: 500px;
    }
    .item{
      width: 1200px;
      height: 500px;
    }
    .down{
      margin-top: 100px;
    }
  </style>
  <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
  <!-- 新 Bootstrap 核心 CSS 文件 -->
  <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
  <title>Daddy图片分享平台</title>
</head>
<body>
<%@ include file="header.jsp"%>
<%
  ArrayList<travelimage> hottestPhotos = new ArrayList<>();
  hottestPhotos = (ArrayList<travelimage>) request.getAttribute("hottestPhotos");
  ArrayList<travelimage> latestPhotos = new ArrayList<>();
  latestPhotos = (ArrayList<travelimage>) request.getAttribute("latestPhotos");

%>
<div class="container">
  <div class="row clearfix">
    <div class="col-md-12 column">
      <div class="carousel slide" id="carousel-583828" data-ride="carousel" data-interval="2500">
        <ol class="carousel-indicators">
          <li class="active" data-slide-to="0" data-target="#carousel-583828">
          </li>
          <li data-slide-to="1" data-target="#carousel-583828">
          </li>
          <li data-slide-to="2" data-target="#carousel-583828">
          </li>
        </ol>
        <div class="carousel-inner">
          <div class="item active">
            <a href="PhotoDetailServlet?ImageID=<%=hottestPhotos.get(0).getImageID()%>">
              <img alt="" class="carousel-inner img-responsive" src="<%=request.getContextPath()%>/PhotoServlet?Path=<%= hottestPhotos.get(0).getPATH()%>"/>
            </a>
            <div class="carousel-caption">
              <h4>
                <%=hottestPhotos.get(0).getTitle()%>
              </h4>
              <p>
                <%=hottestPhotos.get(0).getDescription()%>
              </p>
            </div>
          </div>
          <div class="item">
            <a href="PhotoDetailServlet?ImageID=<%=hottestPhotos.get(1).getImageID()%>">

              <img alt="" class="carousel-inner img-responsive" src="<%=request.getContextPath()%>/PhotoServlet?Path=<%= hottestPhotos.get(1).getPATH()%>" />
            </a>
            <div class="carousel-caption">
              <h4>
                <%=hottestPhotos.get(1).getTitle()%>
              </h4>
              <p>
                <%=hottestPhotos.get(1).getDescription()%>
              </p>
            </div>
          </div>
          <div class="item">
            <a href="PhotoDetailServlet?ImageID=<%=hottestPhotos.get(2).getImageID()%>">

              <img alt="" class="carousel-inner img-responsive" src="<%=request.getContextPath()%>/PhotoServlet?Path=<%= hottestPhotos.get(2).getPATH()%>"  />
            </a>
            <div class="carousel-caption">
              <h4>
                <%=hottestPhotos.get(2).getTitle()%>
              </h4>
              <p>
                <%=hottestPhotos.get(2).getDescription()%>
              </p>
            </div>
          </div>
        </div> <a class="left carousel-control" href="#carousel-583828" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a> <a class="right carousel-control" href="#carousel-583828" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
      </div>
    </div>
  </div>
  <div class="container down">
  <div class="row clearfix">
    <div class="col-md-4 column">
      <img alt=""  src="<%=request.getContextPath()%>/PhotoServlet?Path=<%= latestPhotos.get(0).getPATH()%>" width="380px" height="248px"/>
      <a href="PhotoDetailServlet?ImageID=<%=latestPhotos.get(0).getImageID()%>">
        <h4>
          <%=latestPhotos.get(0).getTitle()%>
        </h4></a>
      <p >
        <%="作者："+latestPhotos.get(0).getAuthor()+"\n"%>
      </p>
      <p>
        <%="主题："+latestPhotos.get(0).getContent()+"\n"%>
      </p>
      <p>
        <%="发布时间："+latestPhotos.get(0).getDateUpdated()+"\n"%>
      </p>
      <p>
      <%="简介："+latestPhotos.get(0).getDescription()+"\n"%>
      </p>

    </div>
    <div class="col-md-4 column">
      <img alt="" src="<%=request.getContextPath()%>/PhotoServlet?Path=<%= latestPhotos.get(1).getPATH()%>" width="380px" height="248px"/>
      <a href="PhotoDetailServlet?ImageID=<%=latestPhotos.get(1).getImageID()%>">
        <h4>
        <%=latestPhotos.get(1).getTitle()%>
      </h4></a>
      <p >
        <%="作者："+latestPhotos.get(1).getAuthor()+"\n"%>
      </p>
      <p>
        <%="主题："+latestPhotos.get(1).getContent()+"\n"%>
      </p>
      <p>
        <%="发布时间："+latestPhotos.get(1).getDateUpdated()+"\n"%>
      </p>
      <p>
        <%="简介："+latestPhotos.get(1).getDescription()+"\n"%>
      </p>
    </div>
    <div class="col-md-4 column">
      <img alt="" src="<%=request.getContextPath()%>/PhotoServlet?Path=<%= latestPhotos.get(2).getPATH()%>" width="380px" height="248px"/>
      <a href="PhotoDetailServlet?ImageID=<%=latestPhotos.get(2).getImageID()%>">
        <h4>
          <%=latestPhotos.get(2).getTitle()%>
        </h4></a>
      <p >
        <%="作者："+latestPhotos.get(2).getAuthor()+"\n"%>
      </p>
      <p>
        <%="主题："+latestPhotos.get(2).getContent()+"\n"%>
      </p>
      <p>
        <%="发布时间："+latestPhotos.get(2).getDateUpdated()+"\n"%>
      </p>
      <p>
        <%="简介："+latestPhotos.get(2).getDescription()+"\n"%>
      </p>
    </div>
  </div>
</div>
  <div class="down">
    <%@ include file="footer.jsp"%>
  </div>
</div>


</body>
</html>