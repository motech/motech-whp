<#import "/spring.ftl" as spring />
<#macro defaultLayout title="WHP">
<!DOCTYPE html>
<html>
<head>
    <title> ${title} </title>
    <#include "scripts.ftl"/>
    <link rel="stylesheet" type="text/css"
          href="<@spring.url '/resources-${applicationVersion}/styles/datepicker.css'/>"/>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/util.js'/>"></script>

</head>
<body>

<div class="row-fluid" id="headerContent">
    <#include "header.ftl"/>
</div>

<div class="container-fluid">

    <div class="row-fluid">
        <div class="span2">
            <#include "../cmfadmin/menu.ftl"/>
        </div>

        <div class="span10" id="mainContent">
            <noscript>
                <div class="row alert alert-error javascript-warning">Please enable Java Script in your browser for the application to work properly. Please contact WHP administrator if you need assistance</div>
            </noscript>
            <!--Body content-->
            <#nested/>
        </div>
    </div>
</div>

<div class="row-fluid" id="footerContent">
    <#include "footer.ftl"/>
</div>

</body>
</html>
</#macro>