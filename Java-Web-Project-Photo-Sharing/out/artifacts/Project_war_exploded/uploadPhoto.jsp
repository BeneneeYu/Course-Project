<%--
  Created by IntelliJ IDEA.
  User: Shen Zhengyu
  Date: 2020/7/11
  Time: 20:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>上传图片</title>
    <!-- 引入 Bootstrap -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body onload="getCountry(`<%=request.getContextPath()%>`)">
<%@ include file="header.jsp" %>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="row clearfix">
                <div class="col-md-6 column">
                    <img alt="待上传" id="prelook" src="images/waiting.png" width="500px" height="500px">
                </div>
                <div class="col-md-6 column">
                    <form role="form" method="post" enctype="multipart/form-data" action="PhotoOperationServlet">
                        <div class="form-group">
                            <label for="Title">标题</label><input required="required"  type="text" class="form-control" id="Title" name="Title"/>
                        </div>
                        <div class="form-group">
                            <label for="Author">作者</label><input required="required" type="text" class="form-control" id="Author" name="Author"/>
                        </div>
                        <div class="form-group">
                            <label for="Content">主题</label><input required="required" type="text" class="form-control" id="Content" name="Content"/>
                        </div>
                        <div class="form-group">
                            <label for="Description">简介</label><input required="required" type="text" class="form-control"
                                                                      id="Description" name="Description"/>
                        </div>
                        <div class="form-group">
                            <label for="country">国家</label>
                            <select required="required" class="form-control" id="country" name="country" onchange="getCity(`<%=request.getContextPath()%>`,this.value)">
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="city">城市</label>
                            <select required="required" class="form-control" id="city" name="city">
                                <option>待选择国家</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="file">选择文件</label><input required="required" type="file" id="file" name="image" onchange="changepic(this)" accept="image/jpg,image/jpeg,image/png,image/PNG"/>
                            <p class="help-block">
                                文件大小不能超过10M，只接受jpg,png,jpeg格式
                            </p>
                        </div>
                        <label>
                            <input type="text" value="uploadPhoto"  name="method" style="display: none" />
                        </label>
                        <a onclick="toSubmit()" class="btn btn-default" id="fakeSubmit">上传</a>
                        <h4>需要确认</h4>

                        <button type="submit" class="btn btn-default" id="realSubmit" style="display: none">确认无误请点这里上传</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <%@ include file="footer.jsp"%>

</div>

</body>
</html>
<script src="https://lib.sinaapp.com/js/jquery/2.0.2/jquery-2.0.2.min.js"></script>
<script>
    function toSubmit() {
        document.getElementById("realSubmit").style.display = "block";
        document.getElementById("fakeSubmit").style.display = "none";
    }
    function getCountry(path) {
        var url = path + "/UtilServlet?action=getCountry";
        $.ajax({
            url: url,   //请求地址
            type: "get",   //请求方法
            dataType: "text",   //期待返回的数据类型，也可以理解为请求的数据类型
            error: function () {  //发生错误时的处理

            },
            success: function (data) {  //成功时的处理。参数表示返回的数据
                var array = data.split(",");
                array.forEach(function (e) {
                    document.getElementById("country").options.add(new Option(e, e));
                })
            }
        })

    }

    function getCity(path,country) {
        var url = path + "/UtilServlet?action=getCity&country=" + country;
        document.getElementById("city").options.length = 0;
        console.log(country)
        $.ajax({
            url: url,   //请求地址
            type: "get",   //请求方法
            dataType: "text",   //期待返回的数据类型，也可以理解为请求的数据类型
            error: function () {  //发生错误时的处理

            },
            success: function (data) {  //成功时的处理。参数表示返回的数据
                var array = data.split(",");
                array.forEach(function (e) {
                    document.getElementById("city").options.add(new Option(e, e));
                })
            }
        })

    }
    function changepic() {
        var reads = new FileReader();
        f = document.getElementById('file').files[0];
        reads.readAsDataURL(f);
        reads.onload = function(e) {
            document.getElementById('prelook').src = this.result;
        };
    }
</script>
