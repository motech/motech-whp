<#import "/spring.ftl" as spring />
<#macro defaultLayout title="WHP">
<!DOCTYPE html>
<html>
<head>
    <title> ${title} </title>
    <link rel="stylesheet" type="text/css" href="<@spring.url '/resources-${applicationVersion}/styles/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<@spring.url '/resources-${applicationVersion}/styles/standard.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<@spring.url '/resources-${applicationVersion}/styles/datepicker.css'/>"/>
    <link rel="SHORTCUT ICON" href="<@spring.url '/resources-${applicationVersion}/images/favicon.ico'/>"/>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/jquery/jquery-1.7.2.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/jquery/jquery.validate.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/bootstrap/bootstrap-modal.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/bootstrap/bootstrap-alerts.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/bootstrap/bootstrap-datepicker.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/bootstrap/bootstrap-transition.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/bootstrap/bootstrap-tooltip.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/util.js'/>"></script>
</head>
<body>
<div class="container">
    <div class="row-fluid" id="headerContent">
        <#setting date_format="yyyy-mm-dd"/>
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