    <div id="footer">
        <div  class="row-fluid">
            <div class="pull-left" id="footer-left-section">

                <a href="<@spring.url '/' />">
                    <img class="pull-left" src="<@spring.url '/resources-${applicationVersion}/images/motechlogo.png'/>"/>
                </a>

                <div class="app-version">
                    <#if Session.version?exists>
                        Version 1.0.${Session.version}
                    </#if>
                </div>
             </div>


            <a class="pull-right" href="<@spring.url '/' />">
                <img  class="pull-right" src="<@spring.url '/resources-${applicationVersion}/images/whplogo.png'/>"/>
            </a>

        </div>
    </div>

