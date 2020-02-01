<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>UnAuthorized</title>
</head>
<body>
<h3>Hello,
    <shiro:principal>
</h3>
<h3><a href="/logout">注销登录</a></h3>
<h3><a shiro:hasRole href="/admin">管理员页面</a></h3>
<h3><a shiro:hasAnyRoles="admin,user" href="/user">普通用户页面</a></h3>
</body>
</html>