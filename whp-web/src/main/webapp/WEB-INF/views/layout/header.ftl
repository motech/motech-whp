<div class="span12">
    <div id="whplogo" class="row">
        <img src="<@spring.url '/resources/images/whplogo.png'/>"/>
        <img class="pull-right" src="<@spring.url '/resources/images/motechlogo.jpg'/>"/>
    </div>
    <div class="row pull-right">
        <a href="<@spring.url '/' />">Home</a>
        <#if Session.loggedInUser?exists>
            | ${Session.loggedInUser.username}
            | <a href="<@spring.url '/security/j_spring_security_logout' />">Logout</a>
        </#if>
    </div>
</div>
