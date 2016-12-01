<%--
  Created by IntelliJ IDEA.
  User: liuxianghong
  Date: 2016/11/30
  Time: 下午4:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>




<div id="sortOrderFileWin" class="easyui-window" title="导入Excel"
     style="width: 500px; height: auto;" closed="true">
    <form name="FormFile" action="/uploadOrder" method="post"  enctype="multipart/form-data">
        <h1>选择上传文件</h1>
        <input type="file" name="file">

        <div style="padding: 5px; text-align: center;">
            <input type="submit" value="upload"/><a href="#" class="easyui-linkbutton" icon="icon-cancel" onclick="javascript:$('#sortOrderFileWin').window('close')">取消</a>
        </div>
    </form>
</div>

<script type="text/javascript">
    function importSortOrder(){
        $("#sortOrderFileWin").window('open');
    }

    function truncateSortOrder(){
        $.messager.confirm('警告','您确定要清空吗',
                function(t) {
                    if (t) {
                        $.ajax({
                            url : '/truncateSortOrder',
                            method : 'POST',
                            dataType : 'json',
                            success : function(r) {
                                if (r==1) {
//                                    $("#dgProduct").datagrid('acceptChanges');
                                    $.messager.show({msg : '',title : '成功'});
//                                    $("#dgProduct").datagrid('reload');
                                } else {
//                                    $("#dgProduct").datagrid('beginEdit',editRow);
                                    $.messager.alert('错误','','error');
                                }
//                                $("#dgProduct").datagrid('unselectAll');
                            }
                        });
                    }
                });
    }
</script>