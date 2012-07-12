<#import "/spring.ftl" as spring />
<#macro defaultLayout title="WHP">
<!DOCTYPE html>
<html>
<head>
    <title> ${title} </title>
    <#include "scripts.ftl"/>
    <link rel="stylesheet" type="text/css" href="<@spring.url '/resources-${applicationVersion}/styles/datepicker.css'/>"/>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/util.js'/>"></script>
</head>
<body>
<div>
    <div class="row-fluid" id="headerContent">
    <#include "header.ftl"/>
</div>

<div id="bodyContent" class="container">
    <div class="row-fluid" id="mainContent">
        <#nested/>
    </div>
</div>

<div class="row-fluid" id="footerContent">
    <#include "footer.ftl"/>
</div>

</body>
</html>
</#macro>