<div class="row white well">
    <div class="span11" id="whplogo">
        <a href="<@spring.url '/' />"><img class="pull-left" src="<@spring.url '/resources/images/motechlogo.jpg'/>"/></a>
        <a href="<@spring.url '/' />"><img class="pull-right" src="<@spring.url '/resources/images/whplogo.png'/>"/></a>
    </div>
    <div id="version" class="span2">
        <#if Session.version?exists>
            Version 1.0.${Session.version}
        </#if>
    </div>
</div>
<div id="links" class="row pull-right">
    <#if Session.loggedInUser?exists>
        ${Session.loggedInUser.username} |
        <a href="<@spring.url '/security/j_spring_security_logout' />">Change Password</a> |
        <a href="<@spring.url '/security/j_spring_security_logout' />">Logout</a>
    </#if>
</div>
