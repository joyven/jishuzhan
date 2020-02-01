<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>books列表</title>
</head>
<body>
<table border="1">
    <tr>
        <th>图书编号</th>
        <th>图书名称</th>
        <th>作者</th>
    </tr>
    <#if books ?? && (books?size>0)>
        <#list books as book >
            <tr>
                <td>${book.id}</td>
                <td>${book.name}</td>
                <td>${book.author}</td>
            </tr>
        </#list>
    </#if>
</table>
</body>
</html>