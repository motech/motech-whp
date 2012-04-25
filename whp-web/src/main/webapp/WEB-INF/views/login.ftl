<#import "layout/default.ftl" as layout>
    <@layout.defaultLayout "Login">

    <div class="span12" id="loginForm">
        <#if RequestParameters.login_error?exists>
            <div class="alert alert-error"> The username or password you entered is incorrect. Please enter the correct credentials. </div>
        </#if>

        <form action="/whp/security/j_spring_security_check" method="POST" class="well form-horizontal">
            <div class="control-group">
                <label class="control-label" for="j_username"> User Name</label>
                <div class="controls">
                    <input class="input-xlarge" type="text" name='j_username' id="j_username" autofocus="autofocus"/>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="j_password">Password</label>
                <div class="controls">
                    <input class="input-xlarge" type="password" name='j_password' id="j_password"/>
                </div>
            </div>

            <button type="submit" class="btn btn-primary"> Login </button>
        </form>
    </div>
</@layout.defaultLayout>
