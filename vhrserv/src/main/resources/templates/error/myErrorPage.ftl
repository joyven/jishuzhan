<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>404动态页面</title>
</head>
<body>
<#if timestamp ??>
    <div>${timestamp?string('yyyy-MM-dd HH:mm:ss')}</div>
</#if>
<#if status ??>
    <div>${status}</div>
</#if>
<#if message ??>
    <div>${message}</div>
</#if>
<h1>ERROR信息可能不会输出，如果用户移除了error数据</h1>
<#if error ??>
    <div>${error}</div>
</#if>

<h1>下面是用户定义的错误数据</h1>
<#if custom_msg ??>
    <div>${custom_msg}</div>
</#if>
</body>
</html>