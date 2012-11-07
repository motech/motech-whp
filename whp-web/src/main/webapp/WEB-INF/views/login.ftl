<#import "/spring.ftl" as spring />
<#import "layout/default-without-menu.ftl" as layout>
<@layout.defaultLayout title="Sign-in to MoTech-WHP">

<div class="row-fluid login-form" id="loginForm">
    <h1><a class="site-name" href="/whp">MoTeCH-WHP</a><span class="module-name">Login</span> </h1>
    <hr />
    <#if RequestParameters.login_error?exists>
        <div id="loginError" class="alert alert-error row">${Session.loginFailure}</div>
    </#if>
    <form action="<@spring.url '/security/j_spring_security_check' />" method="POST" class="">
        <div class="login-controls">

            <div class="control-group">
                <label class="control-label" for="j_username"> User Name</label>

                <div class="controls">
                    <input class="input-xlarge" type="text" name='j_username' id="j_username" autofocus="autofocus" placeholder="" />
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="j_password">Password</label>

                <div class="controls">
                    <input class="input-xlarge" type="password" name='j_password' id="j_password" placeholder=""/>
                </div>
            </div>

            <div class="control-group">
                <div class="controls">
                    <button id="login" type="submit" class="btn btn-large btn-info login-btn"> SIGN IN</button>
                </div>
            </div>
        </div>
    </form>
</div>
</@layout.defaultLayout>
