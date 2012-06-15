<#assign isAdmin = false/>
<#if Session.loggedInUser?exists>
<#list Session.loggedInUser.roles as role>
    <#if role.name == "CMF_ADMIN">
        <#include "../user/changePassword.ftl"/>
        <#assign isAdmin=true/>
    </#if>
</#list>
</#if>
<div id="links" class="navbar">
    <div id="navibar" class="navbar-inner navbar-fixed-top">
        <a href="/whp" class="brand pull-left">MoTeCH-WHP</a>
        <div class="container">
            <div class="nav-collapse collapse">
            <#if Session.loggedInUser?exists>
                <ul class="nav pull-right">
                    <li><a href="#" id="welcome-message">Welcome ${Session.loggedInUser.username}</a></li>
                    <li><a id="home"  href="<@spring.url ''/>">Home</a></li>
                    <li>
                        <#if isAdmin>
                        <a id="changePasswordLink" data-toggle="modal" href="#changePasswordModal">Change password</a>
                        </#if>
                    </li>
                    <li><a id="logout" href="<@spring.url '/security/j_spring_security_logout' />">Logout</a></li>
                </ul>
            </#if>
            </div>
        </div>
    </div>
    <#if Session.loggedInUser?exists && isAdmin>
        <@changePassword/>
    </#if>
</div>

