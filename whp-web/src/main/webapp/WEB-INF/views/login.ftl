<#import "/spring.ftl" as spring />
<#import "layout/default.ftl" as layout>
<@layout.defaultLayout "Login">

<div class="row-fluid" id="loginForm">
    <#if RequestParameters.login_error?exists>
        <div id="loginError" class="alert alert-error row">${Session.loginFailure}</div>
    </#if>
    <form action="<@spring.url '/security/j_spring_security_check' />" method="POST" class="row well form-horizontal">
        <div class="offset2">
            </br>
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

            <div class="control-group">
                <div class="controls">
                    <button id="login" type="submit" class="btn btn-primary login-btn"> Login</button>
                </div>
            </div>
        </div>
    </form>
</div>
</@layout.defaultLayout>
