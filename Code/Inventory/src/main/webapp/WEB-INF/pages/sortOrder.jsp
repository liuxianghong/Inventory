<%--
  Created by IntelliJ IDEA.
  User: liuxianghong
  Date: 2016/11/30
  Time: 下午4:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div id="toolbarSortOrder">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true" onclick="importSortOrder()">导入</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="exportSortOrder()">导出</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="truncateSortOrder()">清空</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="editnum()">设置</a>
</div>

<div id="SortOrderFileWin" class="easyui-window" title="导入Excel"
     style="width: 500px; height: auto;" closed="true">
    <form name="FormFile" action="/uploadOrder" method="post"  enctype="multipart/form-data">
        <h1>选择上传文件</h1>
        <input type="file" name="file">

        <div style="padding: 5px; text-align: center;">
            <input type="submit" value="upload"/><a href="#" class="easyui-linkbutton" icon="icon-cancel" onclick="javascript:$('#SortOrderFileWin').window('close')">取消</a>
        </div>
    </form>
</div>


<!-- 用户信息窗口 -->
<div id="sortOrderMunWin" class="easyui-window" title="订单分隔产品数量"
     style="width: 500px; height: auto;" closed="true">
    <form id="sortOrderMunForm" style="padding: 10px 20px 10px 40px;" method="post">
        <table align="center">
            <tr>
                <td>分隔产品数量:</td>
                <td><input class="easyui-textbox" type="text" name="pickOrderMun" style="width:310px" data-options="required:true,validType:'length[0,100]'"/></td>
            </tr>
        </table>

        <div style="padding: 5px; text-align: center;">
            <a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="javascript:savenum()">确定</a> <a
                href="#" class="easyui-linkbutton" icon="icon-cancel" onclick="javascript:$('#sortOrderMunWin').window('close')">取消</a>
        </div>
    </form>
</div>

<script type="text/javascript">

    $(function(){
        $("#dgSortOrder").datagrid({
            url:"/getAllPickSkuE",
            columns:[[
                {field:'productName',title:'产品名称',width:150},
                {field:'size',title:'规格',width:150},
                {field:'count',title:'数量',width:100},
                {field:'seriesNo',title:'条形码',width:200},
                {field:'locationNo',title:'库位',width:150}
            ]],
            toolbar:'#toolbarSortOrder',  //表格菜单
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
    
    function importSortOrder(){
        $("#SortOrderFileWin").window('open');
    }

    function exportSortOrder(){

        var url="/exportSortOrder";
        window.open(url);

    }

    function truncateSortOrder(){
        $.messager.confirm('警告','您确定要清空吗',
                function(t) {
                    if (t) {
                        $.ajax({
                            url : '/truncateOrder',
                            method : 'POST',
                            dataType : 'json',
                            success : function(r) {
                                if (r==0) {
                                    $("#dgSortOrder").datagrid('acceptChanges');
                                    $.messager.show({msg : '',title : '成功'});
                                    $("#dgSortOrder").datagrid('reload');
                                } else {
                                    $("#dgSortOrder").datagrid('beginEdit',editRow);
                                    $.messager.alert('错误','','error');
                                }
                                $("#dgSortOrder").datagrid('unselectAll');
                            }
                        });
                    }
                });
    }

    function editnum(){
        $("#sortOrderMunWin").window('open');
        $("#sortOrderMunForm").form("load", row);
    }

    function savenum(){
        $('#sortOrderMunForm').form('submit',{
            url: '/savePickOrderMun',
            method:'post',
            success:function(data){
                var r = data;
                if(r==1){
                    $.messager.alert({
                        msg : "操作成功",
                        title : '成功'
                    });
                    $('#sortOrderMunWin').window('close');
                }else{
                    $.messager.alert(
                            '错误',
                            "操作失败",
                            'error');
                    $("#sortOrderMunForm").form("clear");
                }
                $('#sortOrderMunWin').window('close');
            }
        });
    }
</script>