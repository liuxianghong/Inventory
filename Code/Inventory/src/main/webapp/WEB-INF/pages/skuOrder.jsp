<%--
  Created by IntelliJ IDEA.
  User: liuxianghong
  Date: 16/12/5
  Time: 上午12:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div id="toolbarSkuOrder">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true" onclick="importSkuOrder()">导入</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="exportSkuOrder()">导出</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="truncateSkuOrder()">清空</a>
</div>

<div id="skuFileWin" class="easyui-window" title="导入Excel"
     style="width: 500px; height: auto;" closed="true">
    <form name="skuFormFile" action="/Inventory/uploadSku" method="post"  enctype="multipart/form-data">
        <h1>选择上传文件</h1>
        <input type="file" name="file">

        <div style="padding: 5px; text-align: center;">
            <input type="submit" value="upload"/><a href="#" class="easyui-linkbutton" icon="icon-cancel" onclick="javascript:$('#skuFileWin').window('close')">取消</a>
        </div>
    </form>
</div>

<script type="text/javascript">

    $(function(){
        $("#dgSkuOrder").datagrid({
            url:ur + "/getAllSkuOrdersE",
            columns:[[
                {field:'orderName',title:'盘点表单名称',width:150},
                {field:'name',title:'产品名称',width:150},
                {field:'size',title:'规格',width:150},
                {field:'count',title:'数量',width:100},
                {field:'seriesNo',title:'条形码',width:200},
                {field:'calculate',title:'盘点',width:200},
                {field:'locationNo',title:'地址',width:150}
            ]],
            toolbar:'#toolbarSkuOrder',  //表格菜单
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

    function importSkuOrder(){
        $("#skuFileWin").window('open');
    }

    function exportSkuOrder(){

        var url=ur+"/exportSkuOrder";
        window.open(url);

    }

    function truncateSkuOrder(){
        $.messager.confirm('警告','您确定要清空吗',
                function(t) {
                    if (t) {
                        $.ajax({
                            url : ur + '/truncateSkuOrder',
                            method : 'POST',
                            dataType : 'json',
                            success : function(r) {
                                if (r==1) {
                                    $("#dgSkuOrder").datagrid('acceptChanges');
                                    $.messager.show({msg : '',title : '成功'});
                                    $("#dgSkuOrder").datagrid('reload');
                                } else {
                                    $("#dgSkuOrder").datagrid('beginEdit',editRow);
                                    $.messager.alert('错误','','error');
                                }
                                $("#dgSkuOrder").datagrid('unselectAll');
                            }
                        });
                    }
                });
    }
</script>