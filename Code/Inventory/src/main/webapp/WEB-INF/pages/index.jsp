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
    <title>Basic Panel - jQuery EasyUI Demo</title>
    <link rel="stylesheet" type="text/css" href="/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="/easyui/themes/color.css">
    <script type="text/javascript" src="/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/easyui/jquery.easyui.min.js"></script>
</head>
<body>
<h1>这里是Inventory首页</h1>

<table id="dg"></table>
<div id="toolbar">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" >添加</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" >编辑</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" >删除</a>
</div>

<script type="text/javascript">
    $(function(){
        $("#dg").datagrid({
            url:"/getAllOrders",
            columns:[[
                {field:'name',title:'name',width:100},
                {field:'format',title:'format',width:100},
                {field:'number',title:'number',width:100},
                {field:'code',title:'code',width:100},
                {field:'site',title:'site',width:100}
            ]],
            toolbar:'#toolbar',  //表格菜单
            loadMsg:'嗖 ----正在火速加载中 -------------', //加载提示
            pagination:true, //显示分页工具栏
            rownumbers:true, //显示行号列
            singleSelect:true,//是允许选择一行
            queryParams:{   //在请求数据是发送的额外参数，如果没有则不用谢
                name:'easyui',
                hhh:'aa'
            }
        });

    });
</script>
</body>
</html>