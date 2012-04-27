<div class="span12">
    <div class="row white well">
        <div id="whplogo">
            <img class="pull-left" src="<@spring.url '/resources/images/motechlogo.jpg'/>"/>
            <img class="pull-right" src="<@spring.url '/resources/images/whplogo.png'/>"/>
        </div>
    </div>
    <div class="row pull-right">
        <#if Session.loggedInUser?exists>
            <a href="<@spring.url '/' />">Home</a>
            | ${Session.loggedInUser.username}
            | <a href="<@spring.url '/security/j_spring_security_logout' />">Logout</a>
        </#if>
    </div>
</div>
