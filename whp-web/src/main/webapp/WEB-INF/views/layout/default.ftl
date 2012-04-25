<#import "/spring.ftl" as spring />
<#macro defaultLayout title="WHP">
    <!DOCTYPE html>
    <html>
        <head>
            <title> ${title} </title>
            <link rel="stylesheet" type="text/css" href="<@spring.url '/resources/styles/bootstrap.css'/>"/>
            <link rel="stylesheet" type="text/css" href="<@spring.url '/resources/styles/standard.css'/>"/>
        </head>
        <body>
            <div class="container">
                <div class="row-fluid" id="headerContent">
                    <#include "header.ftl"/>
                </div>

                <div class="row-fluid" id="mainContent">
                    <#nested/>
                </div>
                <div class="row-fluid" id="footerContent">
                    <#include "footer.ftl"/>
                </div>
            </div>
        </body>
    </html>
</#macro>