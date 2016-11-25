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

<div style="margin:20px 0;"></div>
<div class="easyui-tabs" data-options="tabWidth:112" style="width:900px;height:900px">
    <div title="订单" style="padding:10px">
        <table id="dg"></table>
    </div>
    <div title="Maps" style="padding:10px">
        <p>Maps Content.</p>
    </div>
    <div title="Journal" style="padding:10px">
        <p>Journal Content.</p>
    </div>
    <div title="Contact" data-options="tabWidth:110" style="padding:10px">
        <p>Contact Content.</p>
    </div>
</div>

<div id="toolbar">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addUserInfo()">添加</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="editUserInfo()">编辑</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="confirm()">删除</a>

</div>

<!-- 用户信息窗口 -->
<div id="oderInfoWin" class="easyui-window" title="订单信息"
     style="width: 500px; height: auto;" closed="true">
    <form id="oderInfoForm" style="padding: 10px 20px 10px 40px;" method="post">
        <input type="hidden" name="id"/>
        <table align="center">
            <tr>
                <td>产品名称:</td>
                <td><input class="easyui-textbox" type="text" name="name" style="width:310px" data-options="required:true,validType:'length[0,100]'"/></td>
            </tr>
            <tr>
                <td>规格:</td>
                <td><input class="easyui-textbox" type="text" name="format" style="width:310px"  data-options="required:true,validType:'length[0,100]'"/></td>
            </tr>
            <tr>
                <td>数量:</td>
                <td><input class="easyui-textbox" type="text" name="number" style="width:310px"  data-options="required:true,validType:'length[0,100]'"/></td>
            </tr>
            <tr>
                <td>条形码:</td>
                <td><input class="easyui-textbox" type="text" name="code" style="width:310px"  data-options="required:true,validType:'length[0,100]'"/></td>
            </tr>
            <tr>
                <td>地址:</td>
                <td><input class="easyui-textbox" type="text" name="site" style="width:310px"  data-options="required:true,validType:'length[0,100]'"/></td>
            </tr>
        </table>

        <div style="padding: 5px; text-align: center;">
            <a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="javascript:saveUsers()">确定</a> <a
                href="#" class="easyui-linkbutton" icon="icon-cancel" onclick="javascript:$('#oderInfoWin').window('close')">取消</a>
        </div>
    </form>
</div>



<script type="text/javascript">

    var dataGrid;

    dataGrid = $(function(){
        $("#dg").datagrid({
            url:"/getAllOrders",
            columns:[[
                {field:'name',title:'产品名称',width:150},
                {field:'format',title:'规格',width:150},
                {field:'number',title:'数量',width:100},
                {field:'code',title:'条形码',width:200},
                {field:'site',title:'地址',width:150}
            ]],
            toolbar:'#toolbar',  //表格菜单
            loadMsg:'------------- 火速加载中 -------------', //加载提示
            pagination:true, //显示分页工具栏
            rownumbers:true, //显示行号列
            singleSelect:true,//是允许选择一行
            queryParams:{   //在请求数据是发送的额外参数，如果没有则不用谢
                name:'easyui',
                hhh:'aa'
            }
        });

    });

    function alertWarning(){
        $.messager.alert('My Title','Here is a warning message!','warning');
    }
    function confirm(){
        $.messager.confirm('My Title', 'Are you confirm this?', function(r){
            if (r){
                alert('confirmed:'+r);
                location.href = 'http://www.google.com';
            }
        });
    }

    function addUserInfo(){
        $("#oderInfoWin").window('open');
        $("#oderInfoForm").form("clear");
    }

    function saveUsers(){
        $('#oderInfoForm').form('submit',{
            url: '/saveOrder',
            method:'post',
            success:function(data){
                var r = data;
                if(r==1){
                    $.messager.alert({
                        msg : "操作成功",
                        title : '成功'
                    });
                    $('#oderInfoWin').window('close');
                    $("#dg").datagrid('reload');
                }else{
                    $.messager.alert(
                            '错误',
                            "操作失败",
                            'error');
                }
                $('#oderInfoWin').window('close');
                $("#dg").datagrid('reload');
            }
        });
    }

    function editUserInfo(){
        var rows = $("#dg").datagrid('getSelections');
        if (rows.length == 1) {
            var row =rows[0];
            $("#oderInfoWin").window('open');
            $("#oderInfoForm").form("load", row);
        }else{
            alert("请选择一条记录！");
            return;
        }
    }
</script>
</body>
</html>