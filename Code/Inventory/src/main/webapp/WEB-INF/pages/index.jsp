<%--
  Created by IntelliJ IDEA.
  User: liuxianghong
  Date: 2016/11/23
  Time: 下午12:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>盘点系统</title>
    <link rel="stylesheet" type="text/css" href="/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="/easyui/themes/color.css">
    <script type="text/javascript" src="/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/easyui/jquery.easyui.min.js"></script>
</head>
<body>

<%@ include file="product.jsp"%>
<%--<%@ include file="order.jsp"%>--%>

<div style="margin:20px 0;"></div>
<div class="easyui-tabs" data-options="tabWidth:112" style="width:900px;height:600px">
    <%--<div title="订单管理" style="padding:10px">--%>
        <%--<table id="dgOrder"></table>--%>
    <%--</div>--%>
    <div title="产品管理" style="padding:10px">
        <table id="dgProduct"></table>
    </div>
</div>

<form name="Form2" action="/upload" method="post"  enctype="multipart/form-data">
    <h1>采用multipart提供的file.transfer方法上传文件</h1>
    <input type="file" name="file">
    <input type="submit" value="upload"/>
</form>

</body>
</html>