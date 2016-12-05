<%--
  Created by IntelliJ IDEA.
  User: liuxianghong
  Date: 16/12/5
  Time: 上午12:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div id="toolbarLocationOrder">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="exportLocationOrder()">导出</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="truncateLocationOrder()">清空</a>
</div>


<script type="text/javascript">

    $(function(){
        $("#dgLocationOrder").datagrid({
            url:"/getAllLocationOrdersE",
            columns:[[
                {field:'orderName',title:'盘点表单名称',width:150},
                {field:'name',title:'产品名称',width:150},
                {field:'size',title:'规格',width:150},
                {field:'count',title:'数量',width:100},
                {field:'seriesNo',title:'条形码',width:200},
                {field:'calculate',title:'盘点',width:200},
                {field:'locationNo',title:'地址',width:150}
            ]],
            toolbar:'#toolbarLocationOrder',  //表格菜单
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


    function exportLocationOrder(){

        var url="/exportLocationOrder";
        window.open(url);

    }

    function truncateLocationOrder(){
        $.messager.confirm('警告','您确定要清空吗',
                function(t) {
                    if (t) {
                        $.ajax({
                            url : '/truncateLocationOrder',
                            method : 'POST',
                            dataType : 'json',
                            success : function(r) {
                                if (r==1) {
                                    $("#dgLocationOrder").datagrid('acceptChanges');
                                    $.messager.show({msg : '',title : '成功'});
                                    $("#dgLocationOrder").datagrid('reload');
                                } else {
                                    $("#dgLocationOrder").datagrid('beginEdit',editRow);
                                    $.messager.alert('错误','','error');
                                }
                                $("#dgLocationOrder").datagrid('unselectAll');
                            }
                        });
                    }
                });
    }
</script>
