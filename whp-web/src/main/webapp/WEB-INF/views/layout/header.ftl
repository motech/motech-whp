<#include "../user/changePassword.ftl">
<div id="links" class="navbar">
    <div id="navibar" class="navbar-inner navbar-fixed-top">
        <div class="container">
            <div class="nav-collapse collapse">
            <#if Session.loggedInUser?exists>
                <ul class="nav pull-left">
                    <li><a href="#">Welcome ${Session.loggedInUser.username?cap_first}</a></li>
                </ul>
                <ul class="nav pull-right">
                    <li><a id="home"  href="/whp">Home</a></li>
                    <li><a id="changePasswordLink" data-toggle="modal" href="#changePasswordModal">Change password</a></li>
                    <li><a id="logout" href="<@spring.url '/security/j_spring_security_logout' />">Logout</a></li>
                    <li><@changePassword/></li>
                </ul>
            <#else>
                <ul class="nav pull-left">
                    <li><a href="#">Welcome</li>
                </ul>
            </#if>
            </div>
        </div>
    </div>
</div>
