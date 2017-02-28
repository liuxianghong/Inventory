<%--
  Created by IntelliJ IDEA.
  User: liuxianghong
  Date: 16/11/25
  Time: 下午11:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="toolbar">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addUserInfo()">添加</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="editOrderInfo()">编辑</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="delOrder()">删除</a>

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

    $(function(){
        $("#dgOrder").datagrid({
            url:ur + "/getAllOrdersE",
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

    function addUserInfo(){
        $("#oderInfoWin").window('open');
        $("#oderInfoForm").form("clear");
    }

    function saveUsers(){
        $('#oderInfoForm').form('submit',{
            url: ur + '/saveOrder',
            method:'post',
            success:function(data){
                var r = data;
                if(r==1){
                    $.messager.alert({
                        msg : "操作成功",
                        title : '成功'
                    });
                    $('#oderInfoWin').window('close');
                    $("#dgOrder").datagrid('reload');
                }else{
                    $.messager.alert(
                            '错误',
                            "操作失败",
                            'error');
                }
                $('#oderInfoWin').window('close');
                $("#dgOrder").datagrid('reload');
            }
        });
    }

    function editOrderInfo(){
        var rows = $("#dgOrder").datagrid('getSelections');
        if (rows.length == 1) {
            var row =rows[0];
            $("#oderInfoWin").window('open');
            $("#oderInfoForm").form("load", row);
        }else{
            alert("请选择一条记录！");
            return;
        }
    }

    function delOrder(){
        var rows = $("#dgOrder").datagrid('getSelections');
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
                                url : ur + '/delOrder',
                                method : 'POST',
                                data : rows[0],
                                dataType : 'json',
                                success : function(r) {
                                    if (r==1) {
                                        $("#dgOrder").datagrid('acceptChanges');
                                        $.messager.show({msg : '',title : '成功'});
                                        $("#dgOrder").datagrid('reload');
                                    } else {
                                        $("#dgOrder").datagrid('beginEdit',editRow);
                                        $.messager.alert('错误','','error');
                                    }
                                    $("#dgOrder").datagrid('unselectAll');
                                }
                            });
                        }
                    });
        }
    }

</script>