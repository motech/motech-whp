<#import "/spring.ftl" as spring />
<#import "header.ftl" as header/>
<#macro defaultLayout entity title="WHP" >
<!DOCTYPE html>
<html ng-app="whp">
<head>
    <title> ${title} </title>
    <#include "scripts.ftl"/>
    <link rel="stylesheet" type="text/css"
          href="<@spring.url '/resources-${applicationVersion}/styles/datepicker.css'/>"/>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/util.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/autoComplete.js'/>"></script>

</head>
<body>

<div class="row-fluid" id="headerContent">
    <@header.header path="../${entity}/menu.ftl"/>
</div>

<div class="row-fluid">
        <div class="container-fluid" id="mainContent">
            <noscript>
                <div class="row alert alert-error javascript-warning">Please enable Java Script in your browser for the application to work properly. Please contact WHP administrator if you need assistance</div>
            </noscript>
           <div id="alertAria"></div>

            <#nested/>
        </div>
</div>

<div class="row-fluid" id="footerContent">
    <#include "footer.ftl"/>
</div>

<!--To trigger file download via JS -->
<iframe id="dummyIframe" frameborder="0" src="" style="visibility: hidden"></iframe>
<script type="text/javascript">
    $('#downloadLinks a').click( function(){
        DownloadAndAlert($(this).attr('href'), $(this).text());
    });
</script>
</body>
</html>
</#macro>