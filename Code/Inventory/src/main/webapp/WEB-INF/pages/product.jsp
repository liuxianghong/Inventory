<%--
  Created by IntelliJ IDEA.
  User: liuxianghong
  Date: 16/11/25
  Time: 下午11:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div id="toolbarProduct">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addProduct()">添加</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="editProduct()">编辑</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="delProduct()">删除</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true" onclick="importProduct()">导入</a>
</div>

<div id="productFileWin" class="easyui-window" title="导入Excel"
     style="width: 500px; height: auto;" closed="true">
    <form name="FormFile" action="/upload" method="post"  enctype="multipart/form-data">
        <h1>选择上传文件</h1>
        <input type="file" name="file">

        <div style="padding: 5px; text-align: center;">
            <input type="submit" value="upload"/><a href="#" class="easyui-linkbutton" icon="icon-cancel" onclick="javascript:$('#productFileWin').window('close')">取消</a>
        </div>
    </form>
</div>


<!-- 用户信息窗口 -->
<div id="productInfoWin" class="easyui-window" title="产品信息"
     style="width: 500px; height: auto;" closed="true">
    <form id="productInfoForm" style="padding: 10px 20px 10px 40px;" method="post">
        <input type="hidden" name="id"/>
        <table align="center">
            <tr>
                <td>产品名称:</td>
                <td><input class="easyui-textbox" type="text" name="name" style="width:310px" data-options="required:true,validType:'length[0,100]'"/></td>
            </tr>
            <tr>
                <td>规格:</td>
                <td><input class="easyui-textbox" type="text" name="size" style="width:310px"  data-options="required:true,validType:'length[0,100]'"/></td>
            </tr>
            <tr>
                <td>数量:</td>
                <td><input class="easyui-textbox" type="text" name="count" style="width:310px"  data-options="required:true,validType:'length[0,100]'"/></td>
            </tr>
            <tr>
                <td>条形码:</td>
                <td><input class="easyui-textbox" type="text" name="seriesNo" style="width:310px"  data-options="required:true,validType:'length[0,100]'"/></td>
            </tr>
            <tr>
                <td>库位:</td>
                <td><input class="easyui-textbox" type="text" name="locationNo" style="width:310px"  data-options="required:true,validType:'length[0,100]'"/></td>
            </tr>
        </table>

        <div style="padding: 5px; text-align: center;">
            <a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="javascript:saveProduct()">确定</a> <a
                href="#" class="easyui-linkbutton" icon="icon-cancel" onclick="javascript:$('#productInfoWin').window('close')">取消</a>
        </div>
    </form>
</div>

<script type="text/javascript">


    $(function(){
        $("#dgProduct").datagrid({
            url:"/getAllGoodsE",
            columns:[[
                {field:'name',title:'产品名称',width:150},
                {field:'size',title:'规格',width:150},
                {field:'count',title:'数量',width:100},
                {field:'seriesNo',title:'条形码',width:200},
                {field:'locationNo',title:'库位',width:150}
            ]],
            toolbar:'#toolbarProduct',  //表格菜单
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

    function addProduct(){
        $("#productInfoWin").window('open');
        $("#productInfoForm").form("clear");
    }

    function saveProduct(){
        $('#productInfoForm').form('submit',{
            url: '/saveGoods',
            method:'post',
            success:function(data){
                var r = data;
                if(r==1){
                    $.messager.alert({
                        msg : "操作成功",
                        title : '成功'
                    });
                    $('#productInfoWin').window('close');
                    $("#dgProduct").datagrid('reload');
                }else{
                    $.messager.alert(
                            '错误',
                            "操作失败",
                            'error');
                }
                $('#productInfoWin').window('close');
                $("#dgProduct").datagrid('reload');
            }
        });
    }

    function editProduct(){
        var rows = $("#dgProduct").datagrid('getSelections');
        if (rows.length == 1) {
            var row =rows[0];
            $("#productInfoWin").window('open');
            $("#productInfoForm").form("load", row);
        }else{
            alert("请选择一条记录！");
            return;
        }
    }

    function importProduct(){
        $("#productFileWin").window('open');
    }

    function delProduct(){
        var rows = $("#dgProduct").datagrid('getSelections');
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
                                url : '/delGoods',
                                method : 'POST',
                                data : rows[0],
                                dataType : 'json',
                                success : function(r) {
                                    if (r==1) {
                                        $("#dgProduct").datagrid('acceptChanges');
                                        $.messager.show({msg : '',title : '成功'});
                                        $("#dgProduct").datagrid('reload');
                                    } else {
                                        $("#dgProduct").datagrid('beginEdit',editRow);
                                        $.messager.alert('错误','','error');
                                    }
                                    $("#dgProduct").datagrid('unselectAll');
                                }
                            });
                        }
                    });
        }
    }
    
</script>

