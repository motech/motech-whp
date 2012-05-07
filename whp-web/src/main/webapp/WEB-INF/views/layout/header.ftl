<#include "../user/changePassword.ftl">
<div class="row white" id="logo">
    <a href="<@spring.url '/' />"><img id="motech-logo" class="pull-left" src="<@spring.url '/resources/images/motechlogo.jpg'/>"/></a>
    <a href="<@spring.url '/' />"><img id="whp-logo" class="pull-right" src="<@spring.url '/resources/images/whplogo.png'/>"/></a>
</div>
<div class="row white" id="version">
    <#if Session.version?exists>
        Version 1.0.${Session.version}
    </#if>
</div>
<div id="links" class="row pull-right">
    <#if Session.loggedInUser?exists>
        ${Session.loggedInUser.username} |
        <a id="changePasswordLink" data-toggle="modal" href="#changePasswordModal">Change Password</a> |
        <a id="logout" href="<@spring.url '/security/j_spring_security_logout' />">Logout</a>
        <@changePassword/>
    </#if>
</div>
