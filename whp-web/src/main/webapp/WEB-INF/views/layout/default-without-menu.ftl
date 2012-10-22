<#import "/spring.ftl" as spring />
<#import "header.ftl" as header/>
<#macro defaultLayout title="WHP">
<!DOCTYPE html>
<html>
<head>
    <title> ${title} </title>
    <#include "scripts.ftl"/>
    <style>
        body {
            background-color: #017AC5;
            background-image: -moz-linear-gradient(center top , #025A91, #0784D2 50%, #009DFE);
            background-image: -webkit-linear-gradient(center top , #025A91, #0784D2 50%, #009DFE);
            background-image: -o-linear-gradient(center top , #025A91, #0784D2 50%, #009DFE);
            background-image: -ms-linear-gradient(center top , #025A91, #0784D2 50%, #009DFE);
            background-image: linear-gradient(center top , #025A91, #0784D2 50%, #009DFE);
            background-repeat: no-repeat;
        }
    </style>


    <script type="text/javascript" src="<@spring.url '/resources-${applicationVersion}/js/util.js'/>"></script>
</head>
<body>

    <div id="bodyContent" class="container">
        <div class="row-fluid" id="mainContent">
            <noscript>
                <div class="row alert alert-error  javascript-warning">Please enable Java Script in your browser for the application to work properly. Please contact WHP administrator if you need assistance</div>
            </noscript>
            <#nested/>
        </div>
    </div>

    <div class="row-fluid" id="footerContent">
        <#include "footer.ftl"/>
    </div>

</body>
</html>
</#macro>