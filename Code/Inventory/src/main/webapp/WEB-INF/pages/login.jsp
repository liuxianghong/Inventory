<%--
  Created by IntelliJ IDEA.
  User: liuxianghong
  Date: 2017/2/21
  Time: 下午4:06
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


<div style="margin:20px 0;"></div>
<div id="loginWin" class="easyui-window" title="登 录" style="width:350px;height:188px;padding:5px;"
     minimizable="false" maximizable="false" resizable="false" collapsible="false">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding:5px;background:#fff;border:1px solid #ccc;">
            <form id="loginForm" method="post">
                <div style="padding:5px 0;">
                    <label for="login">帐 号:</label>
                    <input type="text" name="userName" style="width:260px;" />
                </div>
                <div style="padding:5px 0;">
                    <label for="password">密 码:</label>
                    <input type="password" name="password" style="width:260px;" />
                </div>
                <div style="padding:5px 0;text-align: center;color: red;" id="showMsg"></div>
            </form>
        </div>
        <div region="south" border="false" style="text-align:right;padding:5px 0;">
            <a class="easyui-linkbutton" iconcls="icon-ok" href="javascript:void(0)" onclick="login()">登录</a>
        </div>
    </div>
</div>

</body>

<script type="text/javascript">

    var ur = '/Inventory'

    document.onkeydown = function (e) {
        var event = e || window.event;
        var code = event.keyCode || event.which || event.charCode;
        if (code == 13) {
            login();
        }
    }
    $(function () {
        $("input[name='login']").focus();
    });
    function cleardata() {
        $('#loginForm').form('clear');
    }
    function login() {
        if ($("input[name='login']").val() == "" || $("input[name='password']").val() == "") {
            $("#showMsg").html("用户名或密码为空，请输入");
            $("input[name='login']").focus();
        } else {
            $("#showMsg").html("登录中。。。。。。");
            //ajax异步提交
            $.ajax({
                url : ur + '/adminLogin',
                method : 'POST',
                data : $("#loginForm").serialize(),
                dataType : 'json',
                success : function(r) {
                    $("#showMsg").html(r['msg']);
                    if (r['result'] == 0) {
                        location.href = "index";
                    }
                }
            });
        }
    }
</script>
</html>
