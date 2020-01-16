<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
</head>
<body>
<h3>Hello,
    <#--<@shiro.principal />-->
    <@shiro.principal property="username"/>
</h3>
<h3>
    <a href="/logout">注销登录</a>
</h3>
<h3>
    <@shiro.hasRole name="ADMIN">
        <a href="/shiro/admin">
            管理员页面
        </a>
    </@shiro.hasRole>
</h3>
<h3>
    <@shiro.hasAnyRoles name="ADMIN,USER">
        <a href="/shiro/user">普通用户页面</a>
    </@shiro.hasAnyRoles>
</h3>
</body>
</html>