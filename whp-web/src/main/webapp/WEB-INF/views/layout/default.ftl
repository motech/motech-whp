<#macro defaultLayout title="WHP">
    <html>
        <title>
            ${title}
        </title>

        <body>
            <div class="header">
                <#include "header.ftl"/>
            </div>
            <div class="mainContent">
                <#nested/>
            </div>
            <div class="footer">
                <#include "footer.ftl"/>
            </div>
        </body>
    </html>
</#macro>