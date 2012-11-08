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
            background-repeat: no-repeat;
            background-color: #096DAB;
            background-image: -webkit-gradient(radial, 80% 20%, 0, 80% 40%, 100, from(#5ABCF8), to(#096DAB)); /* Safari 4-5, Chrome 1-9 */
            background-image: -webkit-radial-gradient(80% 20%, closest-corner, #5ABCF8, #096DAB); /* Safari 5.1+, Chrome 10+ */
            background-image: -moz-radial-gradient(80% 20%, closest-corner, #5ABCF8, #096DAB); /* Firefox 3.6+ */
            background-image: -ms-radial-gradient(80% 20%, closest-corner, #5ABCF8, #096DAB);  /* IE 10 */

            background-image: radial-gradient(80% 20%, closest-corner, #5ABCF8, #096DAB); /* w3c */

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