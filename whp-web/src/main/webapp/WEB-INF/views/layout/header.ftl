<#macro header title="WHP-Header" path="">
<#include "../user/changePassword.ftl">
<div id="links" class="navbar navbar-fixed-top">
    <div id="navbarInner" class="navbar-inner">
        <a href="/whp" class="brand pull-left">MoTeCH-WHP</a>

            <#if path != "">
                <#include path>
            </#if>

                <#if Session.loggedInUser?exists>
                    <ul class="nav pull-right">
                        <li class="dropdown pull-right">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">Welcome ${Session.loggedInUser.userName} <b class="caret"></b></a>
                            <ul class="dropdown-menu" role="menu" aria-labelledby="Menu">
                                <li>
                                    <a id="changePasswordLink" data-toggle="modal" href="#changePasswordModal">Change password</a>
                                <li>
                            </ul>
                        </li>
                        <li><a id="logout" href="<@spring.url '/security/j_spring_security_logout' />"><i class="icon-off icon-white"></i> Logout</a></li>
                    </ul>

                </#if>

    </div>
    <#if Session.loggedInUser?exists>
        <@changePassword/>
    </#if>
</div>
</#macro>

