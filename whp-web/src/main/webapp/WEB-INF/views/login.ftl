<#import "layout/default.ftl" as layout>
<@layout.defaultLayout "Login">
<form action="/whp/security/j_spring_security_check" method="POST">
    <input type="textbox" name='j_username' value="" />
    <input type="password" name = "j_password" value="" />
    <input type="submit" name = "login" value="" />
</form>
</@layout.defaultLayout>
