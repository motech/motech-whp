<#import "/spring.ftl" as spring />
<#macro defaultLayout title="WHP">
<!DOCTYPE html>
<html>
<head>
    <title> ${title} </title>
    <link rel="stylesheet" type="text/css" href="<@spring.url '/resources/styles/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<@spring.url '/resources/styles/standard.css'/>"/>
    <link rel="SHORTCUT ICON" href="<@spring.url '/resources/images/favicon.ico'/>"/>
    <script type="text/javascript" src="<@spring.url '/resources/js/jquery/jquery-1.7.2.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources/js/jquery/jquery.validate.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources/js/bootstrap/bootstrap-modal.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources/js/bootstrap/bootstrap-alerts.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources/js/util.js'/>"></script>
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