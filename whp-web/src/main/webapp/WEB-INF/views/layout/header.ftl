<#macro header title="WHP-Header" path="">
    <#include "../user/changePassword.ftl">
    <#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />

<div id="links" class="navbar navbar-fixed-top">
    <div id="navbarInner" class="navbar-inner">
        <a href="/whp" class="brand pull-left">MoTeCH-WHP</a>

        <#if path != "">
            <#include path>
        </#if>

        <#if Session.loggedInUser?exists>
            <ul class="nav pull-right">
            <@security.authorize ifAnyGranted="IT_ADMIN, CMF_ADMIN">
                <li>
                    <a id="reportsUrl" href="${whpReportsURL}" target="_blank" >Reports</a>
                </li>
            </@security.authorize>
                <li class="dropdown pull-right">
                    <@security.authorize ifNotGranted="PROVIDER, FIELD_STAFF">
                        <a class="dropdown-toggle" data-toggle="dropdown"
                           href="#">Welcome ${Session.loggedInUser.userName}
                            <b class="caret"></b></a>
                        <ul class="dropdown-menu" role="menu" aria-labelledby="Menu">
                            <li>
                                <a id="changePasswordLink" data-toggle="modal" href="#changePasswordModal">Change
                                    password</a>
                            <li>
                        </ul>
                    </@security.authorize>
                    <@security.authorize ifAnyGranted="PROVIDER, FIELD_STAFF">
                        <a>Welcome ${Session.loggedInUser.userName}</a>
                    </@security.authorize>

                </li>
                <li><a id="logout" href="<@spring.url '/security/j_spring_security_logout' />"><i
                        class="icon-off icon-white"></i> Logout</a></li>
            </ul>

        </#if>

    </div>
    <#if Session.loggedInUser?exists>

    </#if>
</div>
    <@changePassword/>
</#macro>

