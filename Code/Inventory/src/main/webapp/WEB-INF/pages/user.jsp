<%--
  Created by IntelliJ IDEA.
  User: liuxianghong
  Date: 16/12/2
  Time: 下午9:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="toolbarUser">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addUserInfo()">添加</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="editUserInfo()">编辑</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="delUser()">删除</a>

</div>


<!-- 用户信息窗口 -->
<div id="userInfoWin" class="easyui-window" title="订单信息"
     style="width: 500px; height: auto;" closed="true">
    <form id="userInfoForm" style="padding: 10px 20px 10px 40px;" method="post">
        <input type="hidden" name="id"/>
        <table align="center">
            <tr>
                <td>用户名:</td>
                <td><input class="easyui-textbox" type="text" name="userName" style="width:310px" data-options="required:true,validType:'length[0,100]'"/></td>
            </tr>
            <tr>
                <td>密码:</td>
                <td><input class="easyui-textbox" type="text" name="password" style="width:310px"  data-options="required:true,validType:'length[0,100]'"/></td>
            </tr>

            <tr>
                <td>昵称:</td>
                <td><input class="easyui-textbox" type="text" name="nickName" style="width:310px"  data-options="required:true,validType:'length[0,100]'"/></td>
            </tr>


        </table>

        <div style="padding: 5px; text-align: center;">
            <a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="javascript:saveUsers()">确定</a> <a
                href="#" class="easyui-linkbutton" icon="icon-cancel" onclick="javascript:$('#userInfoWin').window('close')">取消</a>
        </div>
    </form>
</div>

<script type="text/javascript">

    $(function(){
        $("#dgUser").datagrid({
            url:"/getAllUsersE",
            columns:[[
                {field:'userName',title:'用户名',width:150},
                {field:'password',title:'密码',width:150},
                {field:'nickName',title:'昵称',width:150}
            ]],
            toolbar:'#toolbarUser',  //表格菜单
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
        $("#userInfoWin").window('open');
        $("#userInfoForm").form("clear");
    }

    function saveUsers(){
        $('#userInfoForm').form('submit',{
            url: '/saveUser',
            method:'post',
            success:function(data){
                var r = data;
                if(r==1){
                    $.messager.alert({
                        msg : "操作成功",
                        title : '成功'
                    });
                    $('#userInfoWin').window('close');
                    $("#dgUser").datagrid('reload');
                }else{
                    $.messager.alert(
                            '错误',
                            "操作失败",
                            'error');
                }
                $('#userInfoWin').window('close');
                $("#dgUser").datagrid('reload');
            }
        });
    }

    function editUserInfo(){
        var rows = $("#dgUser").datagrid('getSelections');
        if (rows.length == 1) {
            var row =rows[0];
            $("#userInfoWin").window('open');
            $("#userInfoForm").form("load", row);
        }else{
            alert("请选择一条记录！");
            return;
        }
    }

    function delUser(){
        var rows = $("#dgUser").datagrid('getSelections');
        if (rows.length <= 0) {
            $.messager.alert('警告', '您没有选择',
                    'error');
        } else if (rows.length > 1) {
            $.messager.alert('警告', '不支持批量删除',
                    'error');
        } else {
            $.messager.confirm('确定','您确定要删除吗',
                    function(t) {
                        if (t) {
                            $.ajax({
                                url : '/delUser',
                                method : 'POST',
                                data : rows[0],
                                dataType : 'json',
                                success : function(r) {
                                    if (r==1) {
                                        $("#dgUser").datagrid('acceptChanges');
                                        $.messager.show({msg : '',title : '成功'});
                                        $("#dgUser").datagrid('reload');
                                    } else {
                                        $("#dgUser").datagrid('beginEdit',editRow);
                                        $.messager.alert('错误','','error');
                                    }
                                    $("#dgUser").datagrid('unselectAll');
                                }
                            });
                        }
                    });
        }
    }

</script>
