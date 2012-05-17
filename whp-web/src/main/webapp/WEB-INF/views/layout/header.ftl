<#include "../user/changePassword.ftl">
<div id="links" class="navbar">
    <div id="navibar" class="navbar-inner navbar-fixed-top">
        <a href="/whp" class="brand pull-left">MoTeCH-WHP</a>
        <div class="container">
            <div class="nav-collapse collapse">
            <#if Session.loggedInUser?exists>
                <ul class="nav pull-right">
                    <li><a href="#" id="welcome-message">Welcome ${Session.loggedInUser.username?cap_first}</a></li>
                    <li><a id="home"  href="/whp">Home</a></li>
                    <li><a id="changePasswordLink" data-toggle="modal" href="#changePasswordModal">Change password</a></li>
                    <li><a id="logout" href="<@spring.url '/security/j_spring_security_logout' />">Logout</a></li>
                </ul>
            </#if>
            </div>
        </div>
    </div>
    <#if Session.loggedInUser?exists>
        <@changePassword/>
    </#if>
</div>

